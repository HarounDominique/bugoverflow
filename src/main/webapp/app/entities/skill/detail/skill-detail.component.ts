import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISkill } from '../skill.model';

@Component({
  selector: 'jhi-skill-detail',
  templateUrl: './skill-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SkillDetailComponent {
  skill = input<ISkill | null>(null);

  previousState(): void {
    window.history.back();
  }
}
