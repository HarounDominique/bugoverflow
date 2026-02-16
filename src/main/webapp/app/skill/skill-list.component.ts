import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { SkillService } from 'app/skill/skill.service';
import { SkillDTO } from 'app/skill/skill.model';


@Component({
  selector: 'app-skill-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './skill-list.component.html'})
export class SkillListComponent implements OnInit, OnDestroy {

  skillService = inject(SkillService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  skills?: SkillDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@skill.delete.success:Skill was removed successfully.`,
      'skill.usuarioSkill.skill.referenced': $localize`:@@skill.usuarioSkill.skill.referenced:This entity is still referenced by Usuario Skill ${details?.id} via field Skill.`,
      'skill.proyecto.skills.referenced': $localize`:@@skill.proyecto.skills.referenced:This entity is still referenced by Proyecto ${details?.id} via field Skills.`,
      'skill.perfilUsuario.skills.referenced': $localize`:@@skill.perfilUsuario.skills.referenced:This entity is still referenced by Perfil Usuario ${details?.id} via field Skills.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.skillService.getAllSkills()
        .subscribe({
          next: (data) => this.skills = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.skillService.deleteSkill(id)
        .subscribe({
          next: () => this.router.navigate(['/skills'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => {
            if (error.error?.code === 'REFERENCED') {
              const messageParts = error.error.message.split(',');
              this.router.navigate(['/skills'], {
                state: {
                  msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                }
              });
              return;
            }
            this.errorHandler.handleServerError(error.error)
          }
        });
  }

}
