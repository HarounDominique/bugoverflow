import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UsuarioSkillService } from 'app/usuario-skill/usuario-skill.service';
import { UsuarioSkillDTO } from 'app/usuario-skill/usuario-skill.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-usuario-skill-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './usuario-skill-add.component.html'
})
export class UsuarioSkillAddComponent implements OnInit {

  usuarioSkillService = inject(UsuarioSkillService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  usuarioValues?: Map<number,string>;
  skillValues?: Map<number,string>;

  addForm = new FormGroup({
    userId: new FormControl(null, [Validators.required]),
    skillId: new FormControl(null, [Validators.required]),
    nivelInteres: new FormControl(null),
    usuario: new FormControl(null, [Validators.required]),
    skill: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@usuarioSkill.create.success:Usuario Skill was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new UsuarioSkillDTO(this.addForm.value);
    this.usuarioSkillService.createUsuarioSkill(data)
        .subscribe({
          next: () => this.router.navigate(['/usuarioSkills'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
