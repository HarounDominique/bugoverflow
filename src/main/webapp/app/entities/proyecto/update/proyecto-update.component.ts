import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { tipocategoria } from 'app/entities/enumerations/tipocategoria.model';
import { tipoestado } from 'app/entities/enumerations/tipoestado.model';
import { ProyectoService } from '../service/proyecto.service';
import { IProyecto } from '../proyecto.model';
import { ProyectoFormGroup, ProyectoFormService } from './proyecto-form.service';

@Component({
  selector: 'jhi-proyecto-update',
  templateUrl: './proyecto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProyectoUpdateComponent implements OnInit {
  isSaving = false;
  proyecto: IProyecto | null = null;
  tipocategoriaValues = Object.keys(tipocategoria);
  tipoestadoValues = Object.keys(tipoestado);

  usersSharedCollection: IUser[] = [];

  protected proyectoService = inject(ProyectoService);
  protected proyectoFormService = inject(ProyectoFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProyectoFormGroup = this.proyectoFormService.createProyectoFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyecto }) => {
      this.proyecto = proyecto;
      if (proyecto) {
        this.updateForm(proyecto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proyecto = this.proyectoFormService.getProyecto(this.editForm);
    if (proyecto.id !== null) {
      this.subscribeToSaveResponse(this.proyectoService.update(proyecto));
    } else {
      this.subscribeToSaveResponse(this.proyectoService.create(proyecto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProyecto>>): void {
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

  protected updateForm(proyecto: IProyecto): void {
    this.proyecto = proyecto;
    this.proyectoFormService.resetForm(this.editForm, proyecto);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      proyecto.autor,
      ...(proyecto.colaboradores ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.proyecto?.autor, ...(this.proyecto?.colaboradores ?? [])),
        ),
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
