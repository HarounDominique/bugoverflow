import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProyecto, NewProyecto } from '../proyecto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProyecto for edit and NewProyectoFormGroupInput for create.
 */
type ProyectoFormGroupInput = IProyecto | PartialWithRequiredKeyOf<NewProyecto>;

type ProyectoFormDefaults = Pick<NewProyecto, 'id' | 'colaboradores'>;

type ProyectoFormGroupContent = {
  id: FormControl<IProyecto['id'] | NewProyecto['id']>;
  titulo: FormControl<IProyecto['titulo']>;
  descripcion: FormControl<IProyecto['descripcion']>;
  urlrepo: FormControl<IProyecto['urlrepo']>;
  categoria: FormControl<IProyecto['categoria']>;
  estado: FormControl<IProyecto['estado']>;
  autor: FormControl<IProyecto['autor']>;
  colaboradores: FormControl<IProyecto['colaboradores']>;
};

export type ProyectoFormGroup = FormGroup<ProyectoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProyectoFormService {
  createProyectoFormGroup(proyecto: ProyectoFormGroupInput = { id: null }): ProyectoFormGroup {
    const proyectoRawValue = {
      ...this.getFormDefaults(),
      ...proyecto,
    };
    return new FormGroup<ProyectoFormGroupContent>({
      id: new FormControl(
        { value: proyectoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titulo: new FormControl(proyectoRawValue.titulo, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      descripcion: new FormControl(proyectoRawValue.descripcion, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      urlrepo: new FormControl(proyectoRawValue.urlrepo, {
        validators: [Validators.maxLength(500)],
      }),
      categoria: new FormControl(proyectoRawValue.categoria, {
        validators: [Validators.required],
      }),
      estado: new FormControl(proyectoRawValue.estado, {
        validators: [Validators.required],
      }),
      autor: new FormControl(proyectoRawValue.autor),
      colaboradores: new FormControl(proyectoRawValue.colaboradores ?? []),
    });
  }

  getProyecto(form: ProyectoFormGroup): IProyecto | NewProyecto {
    return form.getRawValue() as IProyecto | NewProyecto;
  }

  resetForm(form: ProyectoFormGroup, proyecto: ProyectoFormGroupInput): void {
    const proyectoRawValue = { ...this.getFormDefaults(), ...proyecto };
    form.reset(
      {
        ...proyectoRawValue,
        id: { value: proyectoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProyectoFormDefaults {
    return {
      id: null,
      colaboradores: [],
    };
  }
}
