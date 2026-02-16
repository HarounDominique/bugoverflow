import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { UsuarioPreferenciaService } from 'app/usuario-preferencia/usuario-preferencia.service';
import { UsuarioPreferenciaDTO } from 'app/usuario-preferencia/usuario-preferencia.model';


@Component({
  selector: 'app-usuario-preferencia-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './usuario-preferencia-list.component.html'})
export class UsuarioPreferenciaListComponent implements OnInit, OnDestroy {

  usuarioPreferenciaService = inject(UsuarioPreferenciaService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  usuarioPreferencias?: UsuarioPreferenciaDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@usuarioPreferencia.delete.success:Usuario Preferencia was removed successfully.`    };
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
    this.usuarioPreferenciaService.getAllUsuarioPreferencias()
        .subscribe({
          next: (data) => this.usuarioPreferencias = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.usuarioPreferenciaService.deleteUsuarioPreferencia(id)
        .subscribe({
          next: () => this.router.navigate(['/usuarioPreferencias'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
