import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPerfilUsuario, NewPerfilUsuario } from '../perfil-usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerfilUsuario for edit and NewPerfilUsuarioFormGroupInput for create.
 */
type PerfilUsuarioFormGroupInput = IPerfilUsuario | PartialWithRequiredKeyOf<NewPerfilUsuario>;

type PerfilUsuarioFormDefaults = Pick<NewPerfilUsuario, 'id'>;

type PerfilUsuarioFormGroupContent = {
  id: FormControl<IPerfilUsuario['id'] | NewPerfilUsuario['id']>;
  nombreVisible: FormControl<IPerfilUsuario['nombreVisible']>;
  bio: FormControl<IPerfilUsuario['bio']>;
  github: FormControl<IPerfilUsuario['github']>;
  webPersonal: FormControl<IPerfilUsuario['webPersonal']>;
  avatarUrl: FormControl<IPerfilUsuario['avatarUrl']>;
  user: FormControl<IPerfilUsuario['user']>;
};

export type PerfilUsuarioFormGroup = FormGroup<PerfilUsuarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PerfilUsuarioFormService {
  createPerfilUsuarioFormGroup(perfilUsuario: PerfilUsuarioFormGroupInput = { id: null }): PerfilUsuarioFormGroup {
    const perfilUsuarioRawValue = {
      ...this.getFormDefaults(),
      ...perfilUsuario,
    };
    return new FormGroup<PerfilUsuarioFormGroupContent>({
      id: new FormControl(
        { value: perfilUsuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombreVisible: new FormControl(perfilUsuarioRawValue.nombreVisible, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      bio: new FormControl(perfilUsuarioRawValue.bio, {
        validators: [Validators.maxLength(500)],
      }),
      github: new FormControl(perfilUsuarioRawValue.github, {
        validators: [Validators.maxLength(500)],
      }),
      webPersonal: new FormControl(perfilUsuarioRawValue.webPersonal, {
        validators: [Validators.maxLength(500)],
      }),
      avatarUrl: new FormControl(perfilUsuarioRawValue.avatarUrl, {
        validators: [Validators.maxLength(500)],
      }),
      user: new FormControl(perfilUsuarioRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getPerfilUsuario(form: PerfilUsuarioFormGroup): IPerfilUsuario | NewPerfilUsuario {
    return form.getRawValue() as IPerfilUsuario | NewPerfilUsuario;
  }

  resetForm(form: PerfilUsuarioFormGroup, perfilUsuario: PerfilUsuarioFormGroupInput): void {
    const perfilUsuarioRawValue = { ...this.getFormDefaults(), ...perfilUsuario };
    form.reset(
      {
        ...perfilUsuarioRawValue,
        id: { value: perfilUsuarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PerfilUsuarioFormDefaults {
    return {
      id: null,
    };
  }
}
