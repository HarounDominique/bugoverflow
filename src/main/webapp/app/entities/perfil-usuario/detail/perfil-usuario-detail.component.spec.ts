import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PerfilUsuarioDetailComponent } from './perfil-usuario-detail.component';

describe('PerfilUsuario Management Detail Component', () => {
  let comp: PerfilUsuarioDetailComponent;
  let fixture: ComponentFixture<PerfilUsuarioDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilUsuarioDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./perfil-usuario-detail.component').then(m => m.PerfilUsuarioDetailComponent),
              resolve: { perfilUsuario: () => of({ id: 8289 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PerfilUsuarioDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerfilUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load perfilUsuario on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PerfilUsuarioDetailComponent);

      // THEN
      expect(instance.perfilUsuario()).toEqual(expect.objectContaining({ id: 8289 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
