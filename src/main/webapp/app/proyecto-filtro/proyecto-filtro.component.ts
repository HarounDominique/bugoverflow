import { Component, EventEmitter, Input, Output } from '@angular/core';
import { tipocategoria } from '../entities/enumerations/tipocategoria.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-proyecto-filtro',
  imports: [CommonModule, FormsModule],
  templateUrl: './proyecto-filtro.component.html',
  styleUrl: './proyecto-filtro.component.scss',
})
export class ProyectoFiltroComponent {
  categorias = Object.keys(tipocategoria) as (keyof typeof tipocategoria)[];

  @Input() categoria: keyof typeof tipocategoria | null = null;
  @Output() categoriaChange = new EventEmitter<keyof typeof tipocategoria | null>();

  onCategoriaChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const value = select.value as keyof typeof tipocategoria | '';

    this.categoriaChange.emit(value || null);
  }
}
