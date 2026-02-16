import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UsuarioPreferenciaService } from 'app/usuario-preferencia/usuario-preferencia.service';
import { UsuarioPreferenciaDTO } from 'app/usuario-preferencia/usuario-preferencia.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-usuario-preferencia-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './usuario-preferencia-edit.component.html'
})
export class UsuarioPreferenciaEditComponent implements OnInit {

  usuarioPreferenciaService = inject(UsuarioPreferenciaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    userId: new FormControl(null, [Validators.required]),
    rol: new FormControl(null, [Validators.required]),
    nivelInteres: new FormControl(null),
    usuario: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@usuarioPreferencia.update.success:Usuario Preferencia was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.usuarioPreferenciaService.getUsuarioValues()
        .subscribe({
          next: (data) => this.usuarioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.usuarioPreferenciaService.getUsuarioPreferencia(this.currentId!)
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
    const data = new UsuarioPreferenciaDTO(this.editForm.value);
    this.usuarioPreferenciaService.updateUsuarioPreferencia(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/usuarioPreferencias'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
