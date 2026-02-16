import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CandidaturaService } from 'app/candidatura/candidatura.service';
import { CandidaturaDTO } from 'app/candidatura/candidatura.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-candidatura-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './candidatura-edit.component.html'
})
export class CandidaturaEditComponent implements OnInit {

  candidaturaService = inject(CandidaturaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  proyectoValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    fecha: new FormControl(null, [Validators.required]),
    estado: new FormControl(null, [Validators.required]),
    usuario: new FormControl(null, [Validators.required]),
    proyecto: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@candidatura.update.success:Candidatura was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.candidaturaService.getCandidatura(this.currentId!)
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
    const data = new CandidaturaDTO(this.editForm.value);
    this.candidaturaService.updateCandidatura(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/candidaturas'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
