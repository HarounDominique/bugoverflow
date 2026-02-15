import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';
import { PerfilUsuarioFormGroup, PerfilUsuarioFormService } from './perfil-usuario-form.service';

@Component({
  selector: 'jhi-perfil-usuario-update',
  templateUrl: './perfil-usuario-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PerfilUsuarioUpdateComponent implements OnInit {
  isSaving = false;
  perfilUsuario: IPerfilUsuario | null = null;

  usersSharedCollection: IUser[] = [];

  protected perfilUsuarioService = inject(PerfilUsuarioService);
  protected perfilUsuarioFormService = inject(PerfilUsuarioFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PerfilUsuarioFormGroup = this.perfilUsuarioFormService.createPerfilUsuarioFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfilUsuario }) => {
      this.perfilUsuario = perfilUsuario;
      if (perfilUsuario) {
        this.updateForm(perfilUsuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const perfilUsuario = this.perfilUsuarioFormService.getPerfilUsuario(this.editForm);
    if (perfilUsuario.id !== null) {
      this.subscribeToSaveResponse(this.perfilUsuarioService.update(perfilUsuario));
    } else {
      this.subscribeToSaveResponse(this.perfilUsuarioService.create(perfilUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfilUsuario>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(perfilUsuario: IPerfilUsuario): void {
    this.perfilUsuario = perfilUsuario;
    this.perfilUsuarioFormService.resetForm(this.editForm, perfilUsuario);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, perfilUsuario.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.perfilUsuario?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
