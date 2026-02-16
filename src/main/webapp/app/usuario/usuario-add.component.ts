import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UsuarioService } from 'app/usuario/usuario.service';
import { UsuarioDTO } from 'app/usuario/usuario.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-usuario-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './usuario-add.component.html'
})
export class UsuarioAddComponent {

  usuarioService = inject(UsuarioService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    login: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    email: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    nombre: new FormControl(null, [Validators.maxLength(50)]),
    apellido: new FormControl(null, [Validators.maxLength(50)]),
    activo: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@usuario.create.success:Usuario was created successfully.`,
      USUARIO_LOGIN_UNIQUE: $localize`:@@Exists.usuario.login:This Login is already taken.`,
      USUARIO_EMAIL_UNIQUE: $localize`:@@Exists.usuario.email:This Email is already taken.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new UsuarioDTO(this.addForm.value);
    this.usuarioService.createUsuario(data)
        .subscribe({
          next: () => this.router.navigate(['/usuarios'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
