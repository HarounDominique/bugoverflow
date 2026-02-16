import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { UsuarioService } from 'app/usuario/usuario.service';
import { UsuarioDTO } from 'app/usuario/usuario.model';


@Component({
  selector: 'app-usuario-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './usuario-list.component.html'})
export class UsuarioListComponent implements OnInit, OnDestroy {

  usuarioService = inject(UsuarioService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  usuarios?: UsuarioDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@usuario.delete.success:Usuario was removed successfully.`,
      'usuario.perfilUsuario.usuario.referenced': $localize`:@@usuario.perfilUsuario.usuario.referenced:This entity is still referenced by Perfil Usuario ${details?.id} via field Usuario.`,
      'usuario.usuarioSkill.usuario.referenced': $localize`:@@usuario.usuarioSkill.usuario.referenced:This entity is still referenced by Usuario Skill ${details?.id} via field Usuario.`,
      'usuario.usuarioPreferencia.usuario.referenced': $localize`:@@usuario.usuarioPreferencia.usuario.referenced:This entity is still referenced by Usuario Preferencia ${details?.id} via field Usuario.`,
      'usuario.proyecto.autor.referenced': $localize`:@@usuario.proyecto.autor.referenced:This entity is still referenced by Proyecto ${details?.id} via field Autor.`,
      'usuario.candidatura.usuario.referenced': $localize`:@@usuario.candidatura.usuario.referenced:This entity is still referenced by Candidatura ${details?.id} via field Usuario.`
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
    this.usuarioService.getAllUsuarios()
        .subscribe({
          next: (data) => this.usuarios = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.usuarioService.deleteUsuario(id)
        .subscribe({
          next: () => this.router.navigate(['/usuarios'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => {
            if (error.error?.code === 'REFERENCED') {
              const messageParts = error.error.message.split(',');
              this.router.navigate(['/usuarios'], {
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
