import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PerfilUsuarioService } from 'app/perfil-usuario/perfil-usuario.service';
import { PerfilUsuarioDTO } from 'app/perfil-usuario/perfil-usuario.model';


@Component({
  selector: 'app-perfil-usuario-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './perfil-usuario-list.component.html'})
export class PerfilUsuarioListComponent implements OnInit, OnDestroy {

  perfilUsuarioService = inject(PerfilUsuarioService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  perfilUsuarios?: PerfilUsuarioDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@perfilUsuario.delete.success:Perfil Usuario was removed successfully.`    };
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
    this.perfilUsuarioService.getAllPerfilUsuarios()
        .subscribe({
          next: (data) => this.perfilUsuarios = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.perfilUsuarioService.deletePerfilUsuario(id)
        .subscribe({
          next: () => this.router.navigate(['/perfilUsuarios'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
