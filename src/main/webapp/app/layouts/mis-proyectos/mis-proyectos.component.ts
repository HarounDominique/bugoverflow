import { Component, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectoDetailComponent } from '../../entities/proyecto/detail/proyecto-detail.component';
import { ProyectoState } from '../../entities/proyecto/state/proyecto.state';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import TranslateDirective from '../../shared/language/translate.directive';
import { IProyecto } from '../../entities/proyecto/proyecto.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'jhi-mis-proyectos',
  standalone: true,
  imports: [CommonModule, ProyectoDetailComponent, FaIconComponent, TranslateDirective, RouterLink],
  templateUrl: './mis-proyectos.component.html',
})
export class MisProyectosComponent {
  state = inject(ProyectoState);

  proyecto = input<IProyecto | null>(null);

  ngOnInit() {
    this.state.cargarMisProyectos();
  }

  previousState(): void {
    window.history.back();
  }
}
