import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PerfilUsuarioService } from 'app/perfil-usuario/perfil-usuario.service';
import { PerfilUsuarioDTO } from 'app/perfil-usuario/perfil-usuario.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-perfil-usuario-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './perfil-usuario-add.component.html'
})
export class PerfilUsuarioAddComponent implements OnInit {

  perfilUsuarioService = inject(PerfilUsuarioService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  skillsValues?: Map<number,string>;

  addForm = new FormGroup({
    nombreVisible: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    bio: new FormControl(null, [Validators.maxLength(500)]),
    github: new FormControl(null, [Validators.maxLength(500)]),
    webPersonal: new FormControl(null, [Validators.maxLength(500)]),
    avatarUrl: new FormControl(null, [Validators.maxLength(500)]),
    usuario: new FormControl(null, [Validators.required]),
    skills: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@perfilUsuario.create.success:Perfil Usuario was created successfully.`,
      PERFIL_USUARIO_USUARIO_UNIQUE: $localize`:@@Exists.perfilUsuario.usuario:This Usuario is already referenced by another Perfil Usuario.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.perfilUsuarioService.getUsuarioValues()
        .subscribe({
          next: (data) => this.usuarioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.perfilUsuarioService.getSkillsValues()
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
    const data = new PerfilUsuarioDTO(this.addForm.value);
    this.perfilUsuarioService.createPerfilUsuario(data)
        .subscribe({
          next: () => this.router.navigate(['/perfilUsuarios'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
