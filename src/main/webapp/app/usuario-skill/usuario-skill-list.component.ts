import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { UsuarioSkillService } from 'app/usuario-skill/usuario-skill.service';
import { UsuarioSkillDTO } from 'app/usuario-skill/usuario-skill.model';


@Component({
  selector: 'app-usuario-skill-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './usuario-skill-list.component.html'})
export class UsuarioSkillListComponent implements OnInit, OnDestroy {

  usuarioSkillService = inject(UsuarioSkillService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  usuarioSkills?: UsuarioSkillDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@usuarioSkill.delete.success:Usuario Skill was removed successfully.`    };
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
    this.usuarioSkillService.getAllUsuarioSkills()
        .subscribe({
          next: (data) => this.usuarioSkills = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.usuarioSkillService.deleteUsuarioSkill(id)
        .subscribe({
          next: () => this.router.navigate(['/usuarioSkills'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
