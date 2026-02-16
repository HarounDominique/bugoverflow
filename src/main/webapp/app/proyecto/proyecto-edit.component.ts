import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProyectoService } from 'app/proyecto/proyecto.service';
import { ProyectoDTO } from 'app/proyecto/proyecto.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-proyecto-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './proyecto-edit.component.html'
})
export class ProyectoEditComponent implements OnInit {

  proyectoService = inject(ProyectoService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  autorValues?: Map<number,string>;
  skillsValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    titulo: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    descripcion: new FormControl(null, [Validators.maxLength(500)]),
    urlRepo: new FormControl(null, [Validators.maxLength(500)]),
    categoria: new FormControl(null, [Validators.maxLength(50)]),
    estado: new FormControl(null, [Validators.required]),
    autor: new FormControl(null, [Validators.required]),
    skills: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@proyecto.update.success:Proyecto was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.proyectoService.getAutorValues()
        .subscribe({
          next: (data) => this.autorValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.proyectoService.getSkillsValues()
        .subscribe({
          next: (data) => this.skillsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.proyectoService.getProyecto(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new ProyectoDTO(this.editForm.value);
    this.proyectoService.updateProyecto(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/proyectos'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
