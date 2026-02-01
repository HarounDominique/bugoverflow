import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountService } from '../../../core/auth/account.service';
import { IProyecto } from '../../proyecto/proyecto.model';
import { map, filter, take, switchMap, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProyectoState {
  private readonly accountService = inject(AccountService);
  private readonly http = inject(HttpClient);

  // Estado PRIVADO (solo writable aquí)
  private readonly _proyectos = signal<IProyecto[]>([]);
  private readonly _activo = signal<IProyecto | null>(null);
  private readonly _loading = signal<boolean>(false);
  private readonly _error = signal<string | null>(null);
  private readonly _selectedProyecto = signal<IProyecto | undefined>(undefined);
  private readonly _userLogin = signal<string | null>(null);

  // Estado PÚBLICO (solo readable)
  readonly proyectos = computed(() => this._proyectos());
  readonly activo = computed(() => this._activo());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());
  readonly selectedProyecto = computed(() => this._selectedProyecto());
  readonly misProyectos = computed(() => this._proyectos());

  // Computed derivados (SIMPLIFICADO)
  readonly proyectosAbiertos = computed(() => this._proyectos().filter(p => p.estado === 'ABIERTO'));

  constructor() {}

  // MÉTODOS de CONTROL de estado
  cargarTodos(): void {
    this._loading.set(true);
    this._error.set(null);

    this.http.get<IProyecto[]>('/api/proyectos').subscribe({
      next: proyectos => {
        this._proyectos.set(proyectos);
        this._loading.set(false);
      },
      error: () => {
        this._error.set('Error cargando proyectos');
        this._loading.set(false);
      },
    });
  }

  cargarMisProyectos(): void {
    this._loading.set(true);
    this._error.set(null);

    this.http.get<IProyecto[]>('/api/proyectos/autor-current').subscribe({
      next: proyectos => {
        this._proyectos.set(proyectos);
        this._loading.set(false);
      },
      error: () => {
        this._error.set('Error cargando tus proyectos');
        this._loading.set(false);
      },
    });
  }

  crear(proyecto: Omit<IProyecto, 'id'>): void {
    this._loading.set(true);
    this.http.post<IProyecto>('/api/proyectos', proyecto).subscribe({
      next: nuevo => {
        this._proyectos.update(pros => [...pros, nuevo]);
        this._loading.set(false);
      },
      error: () => {
        this._error.set('Error creando proyecto');
        this._loading.set(false);
      },
    });
  }

  actualizar(proyecto: IProyecto): void {
    this._proyectos.update(pros => pros.map(p => (p.id === proyecto.id ? proyecto : p)));
  }

  seleccionar(proyecto: IProyecto | undefined): void {
    this._selectedProyecto.set(proyecto);
  }

  limpiarError(): void {
    this._error.set(null);
  }

  actualizarUsuario(): void {
    this.accountService
      .identity()
      .pipe(
        take(1),
        map(account => account?.login || null),
      )
      .subscribe(login => {
        this._userLogin.set(login);
      });
  }

  //GETTER para uso en componentes (misProyectos simplificado)
  getMisProyectos(): IProyecto[] {
    const user = this.accountService.getAuthenticationState();
    const userLogin = user instanceof Observable ? 'admin' : (user as any)?.login || 'admin';
    return this._proyectos().filter(p => p.autor?.login === userLogin);
  }

  eliminar(proyectoId: number): void {
    this._loading.set(true);
    this._error.set(null);

    this.http.delete(`/api/proyectos/${proyectoId}`).subscribe({
      next: () => {
        // Elimina del estado local
        this._proyectos.update(pros => pros.filter(p => p.id !== proyectoId));

        // Si era el proyecto seleccionado, lo deselecciona
        if (this._selectedProyecto()?.id === proyectoId) {
          this._selectedProyecto.set(undefined);
        }

        this._loading.set(false);
      },
      error: error => {
        this._error.set('Error eliminando proyecto');
        console.error('Delete error:', error);
        this._loading.set(false);
      },
    });
  }
}
