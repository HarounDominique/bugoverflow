import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectoDetailComponent } from '../../entities/proyecto/detail/proyecto-detail.component';
import { ProyectoState } from '../../entities/proyecto/state/proyecto.state';

@Component({
  selector: 'jhi-mis-proyectos',
  standalone: true,
  imports: [CommonModule, ProyectoDetailComponent],
  templateUrl: './mis-proyectos.component.html',
})
export class MisProyectosComponent {
  state = inject(ProyectoState);

  ngOnInit() {
    this.state.cargarMisProyectos();
  }
}
