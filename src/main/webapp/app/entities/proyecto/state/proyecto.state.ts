import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountService } from '../../../core/auth/account.service';
import { IProyecto } from '../../proyecto/proyecto.model';
import { map, take } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { tipocategoria } from '../../enumerations/tipocategoria.model';

@Injectable({ providedIn: 'root' })
export class ProyectoState {
  private readonly accountService = inject(AccountService);
  private readonly http = inject(HttpClient);

  // Estado PRIVADO
  private readonly _proyectos = signal<IProyecto[]>([]);
  private readonly _proyectosComunidad = signal<IProyecto[]>([]);
  private readonly _activo = signal<IProyecto | null>(null);
  private readonly _loading = signal<boolean>(false);
  private readonly _error = signal<string | null>(null);
  private readonly _selectedMiProyecto = signal<IProyecto | undefined>(undefined);
  private readonly _selectedProyectoComunidad = signal<IProyecto | undefined>(undefined);
  private readonly _userLogin = signal<string | null>(null);
  private readonly _categoria = signal<keyof typeof tipocategoria | null>(null);

  // Estado PÚBLICO
  readonly proyectos = computed(() => this._proyectos());
  readonly activo = computed(() => this._activo());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());
  readonly selectedMiProyecto = computed(() => this._selectedMiProyecto());
  readonly selectedProyectoComunidad = computed(() => this._selectedProyectoComunidad());
  readonly misProyectos = computed(() => this._proyectos());
  readonly proyectosComunidad = computed(() => this._proyectosComunidad());
  readonly categoria = computed(() => this._categoria());

  // Computed derivados
  readonly proyectosAbiertos = computed(() => this._proyectos().filter(p => p.estado === 'ABIERTO'));

  constructor() {}

  // MÉTODOS
  cargarTodos(): void {
    this._loading.set(true);
    this._error.set(null);

    this.http.get<IProyecto[]>('/api/proyectos').subscribe({
      next: proyectos => {
        this._proyectos.set(proyectos);
        this._loading.set(false);
        this._selectedMiProyecto.set(undefined);
        this._selectedProyectoComunidad.set(undefined);
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
    this._selectedMiProyecto.set(undefined); // reset al cargar lista

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

  cargarProyectosComunidad(): void {
    this._loading.set(true);
    this._error.set(null);
    this._selectedProyectoComunidad.set(undefined); // reset al cargar lista

    const params: any = {};
    if (this._categoria()) {
      params.categoria = this._categoria();
    }

    this.http.get<IProyecto[]>('/api/proyectos/comunidad', { params }).subscribe({
      next: proyectos => {
        this._proyectosComunidad.set(proyectos);
        this._loading.set(false);
      },
      error: () => {
        this._error.set('Error cargando proyectos de la comunidad');
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
    this._proyectosComunidad.update(pros => pros.map(p => (p.id === proyecto.id ? proyecto : p)));
  }

  seleccionar(proyecto: IProyecto | undefined, tipo: 'mi' | 'comunidad'): void {
    if (tipo === 'mi') {
      this._selectedMiProyecto.set(proyecto);
      this._selectedProyectoComunidad.set(undefined);
    } else {
      this._selectedProyectoComunidad.set(proyecto);
      this._selectedMiProyecto.set(undefined);
    }
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
        this._proyectos.update(pros => pros.filter(p => p.id !== proyectoId));
        this._proyectosComunidad.update(pros => pros.filter(p => p.id !== proyectoId));

        if (this._selectedMiProyecto()?.id === proyectoId) {
          this._selectedMiProyecto.set(undefined);
        }
        if (this._selectedProyectoComunidad()?.id === proyectoId) {
          this._selectedProyectoComunidad.set(undefined);
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

  setCategoria(categoria: keyof typeof tipocategoria | null): void {
    this._categoria.set(categoria);
    this.cargarProyectosComunidad();
  }
}
