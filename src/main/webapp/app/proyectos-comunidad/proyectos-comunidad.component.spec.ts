import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProyectosComunidadComponent } from './proyectos-comunidad.component';

describe('ProyectosComunidadComponent', () => {
  let component: ProyectosComunidadComponent;
  let fixture: ComponentFixture<ProyectosComunidadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProyectosComunidadComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProyectosComunidadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
