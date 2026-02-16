import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UsuarioSkillService } from 'app/usuario-skill/usuario-skill.service';
import { UsuarioSkillDTO } from 'app/usuario-skill/usuario-skill.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-usuario-skill-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './usuario-skill-edit.component.html'
})
export class UsuarioSkillEditComponent implements OnInit {

  usuarioSkillService = inject(UsuarioSkillService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  skillValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    userId: new FormControl(null, [Validators.required]),
    skillId: new FormControl(null, [Validators.required]),
    nivelInteres: new FormControl(null),
    usuario: new FormControl(null, [Validators.required]),
    skill: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@usuarioSkill.update.success:Usuario Skill was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.usuarioSkillService.getUsuarioValues()
        .subscribe({
          next: (data) => this.usuarioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.usuarioSkillService.getSkillValues()
        .subscribe({
          next: (data) => this.skillValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.usuarioSkillService.getUsuarioSkill(this.currentId!)
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
    const data = new UsuarioSkillDTO(this.editForm.value);
    this.usuarioSkillService.updateUsuarioSkill(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/usuarioSkills'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
