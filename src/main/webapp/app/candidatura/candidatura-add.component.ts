import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CandidaturaService } from 'app/candidatura/candidatura.service';
import { CandidaturaDTO } from 'app/candidatura/candidatura.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-candidatura-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './candidatura-add.component.html'
})
export class CandidaturaAddComponent implements OnInit {

  candidaturaService = inject(CandidaturaService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  proyectoValues?: Map<number,string>;

  addForm = new FormGroup({
    fecha: new FormControl(null, [Validators.required]),
    estado: new FormControl(null, [Validators.required]),
    usuario: new FormControl(null, [Validators.required]),
    proyecto: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@candidatura.create.success:Candidatura was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.candidaturaService.getUsuarioValues()
        .subscribe({
          next: (data) => this.usuarioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.candidaturaService.getProyectoValues()
        .subscribe({
          next: (data) => this.proyectoValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CandidaturaDTO(this.addForm.value);
    this.candidaturaService.createCandidatura(data)
        .subscribe({
          next: () => this.router.navigate(['/candidaturas'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
