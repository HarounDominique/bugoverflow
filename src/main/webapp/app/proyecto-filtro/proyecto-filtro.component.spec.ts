import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProyectoFiltroComponent } from './proyecto-filtro.component';

describe('ProyectoFiltroComponent', () => {
  let component: ProyectoFiltroComponent;
  let fixture: ComponentFixture<ProyectoFiltroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProyectoFiltroComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProyectoFiltroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
