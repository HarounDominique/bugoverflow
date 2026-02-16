import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { SkillService } from 'app/skill/skill.service';
import { SkillDTO } from 'app/skill/skill.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-skill-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './skill-add.component.html'
})
export class SkillAddComponent {

  skillService = inject(SkillService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    nombre: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    categoria: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@skill.create.success:Skill was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new SkillDTO(this.addForm.value);
    this.skillService.createSkill(data)
        .subscribe({
          next: () => this.router.navigate(['/skills'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
