import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPerfilUsuario } from 'app/entities/perfil-usuario/perfil-usuario.model';
import { PerfilUsuarioService } from 'app/entities/perfil-usuario/service/perfil-usuario.service';
import { ISkill } from '../skill.model';
import { SkillService } from '../service/skill.service';
import { SkillFormGroup, SkillFormService } from './skill-form.service';

@Component({
  selector: 'jhi-skill-update',
  templateUrl: './skill-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SkillUpdateComponent implements OnInit {
  isSaving = false;
  skill: ISkill | null = null;

  perfilUsuariosSharedCollection: IPerfilUsuario[] = [];

  protected skillService = inject(SkillService);
  protected skillFormService = inject(SkillFormService);
  protected perfilUsuarioService = inject(PerfilUsuarioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SkillFormGroup = this.skillFormService.createSkillFormGroup();

  comparePerfilUsuario = (o1: IPerfilUsuario | null, o2: IPerfilUsuario | null): boolean =>
    this.perfilUsuarioService.comparePerfilUsuario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ skill }) => {
      this.skill = skill;
      if (skill) {
        this.updateForm(skill);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const skill = this.skillFormService.getSkill(this.editForm);
    if (skill.id !== null) {
      this.subscribeToSaveResponse(this.skillService.update(skill));
    } else {
      this.subscribeToSaveResponse(this.skillService.create(skill));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkill>>): void {
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

  protected updateForm(skill: ISkill): void {
    this.skill = skill;
    this.skillFormService.resetForm(this.editForm, skill);

    this.perfilUsuariosSharedCollection = this.perfilUsuarioService.addPerfilUsuarioToCollectionIfMissing<IPerfilUsuario>(
      this.perfilUsuariosSharedCollection,
      ...(skill.perfilUsuarios ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.perfilUsuarioService
      .query()
      .pipe(map((res: HttpResponse<IPerfilUsuario[]>) => res.body ?? []))
      .pipe(
        map((perfilUsuarios: IPerfilUsuario[]) =>
          this.perfilUsuarioService.addPerfilUsuarioToCollectionIfMissing<IPerfilUsuario>(
            perfilUsuarios,
            ...(this.skill?.perfilUsuarios ?? []),
          ),
        ),
      )
      .subscribe((perfilUsuarios: IPerfilUsuario[]) => (this.perfilUsuariosSharedCollection = perfilUsuarios));
  }
}
