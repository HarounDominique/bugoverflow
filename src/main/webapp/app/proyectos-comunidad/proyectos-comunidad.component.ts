import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProyectoState } from '../entities/proyecto/state/proyecto.state';
import { IProyecto } from '../entities/proyecto/proyecto.model';
import { ProyectoDetailComponent } from '../entities/proyecto/detail/proyecto-detail.component';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { ProyectoFiltroComponent } from '../proyecto-filtro/proyecto-filtro.component';

@Component({
  selector: 'jhi-proyectos-comunidad',
  imports: [RouterLink, ProyectoDetailComponent, FaIconComponent, ProyectoFiltroComponent],
  templateUrl: './proyectos-comunidad.component.html',
  styleUrl: './proyectos-comunidad.component.scss',
})
export class ProyectosComunidadComponent {
  state = inject(ProyectoState);

  proyecto = input<IProyecto | null>(null);

  ngOnInit() {
    this.state.cargarProyectosComunidad();
  }

  previousState(): void {
    window.history.back();
  }
}
