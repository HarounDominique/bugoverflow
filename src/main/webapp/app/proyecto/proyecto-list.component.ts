import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ProyectoService } from 'app/proyecto/proyecto.service';
import { ProyectoDTO } from 'app/proyecto/proyecto.model';


@Component({
  selector: 'app-proyecto-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './proyecto-list.component.html'})
export class ProyectoListComponent implements OnInit, OnDestroy {

  proyectoService = inject(ProyectoService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  proyectoes?: ProyectoDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@proyecto.delete.success:Proyecto was removed successfully.`,
      'proyecto.candidatura.proyecto.referenced': $localize`:@@proyecto.candidatura.proyecto.referenced:This entity is still referenced by Candidatura ${details?.id} via field Proyecto.`
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
    this.proyectoService.getAllProyectoes()
        .subscribe({
          next: (data) => this.proyectoes = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.proyectoService.deleteProyecto(id)
        .subscribe({
          next: () => this.router.navigate(['/proyectos'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => {
            if (error.error?.code === 'REFERENCED') {
              const messageParts = error.error.message.split(',');
              this.router.navigate(['/proyectos'], {
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
