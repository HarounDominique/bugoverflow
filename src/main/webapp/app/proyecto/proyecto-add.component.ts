import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProyectoService } from 'app/proyecto/proyecto.service';
import { ProyectoDTO } from 'app/proyecto/proyecto.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-proyecto-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './proyecto-add.component.html'
})
export class ProyectoAddComponent implements OnInit {

  proyectoService = inject(ProyectoService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  autorValues?: Map<number,string>;
  skillsValues?: Map<number,string>;

  addForm = new FormGroup({
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
      created: $localize`:@@proyecto.create.success:Proyecto was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ProyectoDTO(this.addForm.value);
    this.proyectoService.createProyecto(data)
        .subscribe({
          next: () => this.router.navigate(['/proyectos'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
