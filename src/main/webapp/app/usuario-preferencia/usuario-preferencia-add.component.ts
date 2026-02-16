import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UsuarioPreferenciaService } from 'app/usuario-preferencia/usuario-preferencia.service';
import { UsuarioPreferenciaDTO } from 'app/usuario-preferencia/usuario-preferencia.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-usuario-preferencia-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './usuario-preferencia-add.component.html'
})
export class UsuarioPreferenciaAddComponent implements OnInit {

  usuarioPreferenciaService = inject(UsuarioPreferenciaService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;

  addForm = new FormGroup({
    userId: new FormControl(null, [Validators.required]),
    rol: new FormControl(null, [Validators.required]),
    nivelInteres: new FormControl(null),
    usuario: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@usuarioPreferencia.create.success:Usuario Preferencia was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.usuarioPreferenciaService.getUsuarioValues()
        .subscribe({
          next: (data) => this.usuarioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new UsuarioPreferenciaDTO(this.addForm.value);
    this.usuarioPreferenciaService.createUsuarioPreferencia(data)
        .subscribe({
          next: () => this.router.navigate(['/usuarioPreferencias'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
