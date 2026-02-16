import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PerfilUsuarioService } from 'app/perfil-usuario/perfil-usuario.service';
import { PerfilUsuarioDTO } from 'app/perfil-usuario/perfil-usuario.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-perfil-usuario-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './perfil-usuario-edit.component.html'
})
export class PerfilUsuarioEditComponent implements OnInit {

  perfilUsuarioService = inject(PerfilUsuarioService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  skillsValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
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
      updated: $localize`:@@perfilUsuario.update.success:Perfil Usuario was updated successfully.`,
      PERFIL_USUARIO_USUARIO_UNIQUE: $localize`:@@Exists.perfilUsuario.usuario:This Usuario is already referenced by another Perfil Usuario.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.perfilUsuarioService.getPerfilUsuario(this.currentId!)
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
    const data = new PerfilUsuarioDTO(this.editForm.value);
    this.perfilUsuarioService.updatePerfilUsuario(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/perfilUsuarios'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
