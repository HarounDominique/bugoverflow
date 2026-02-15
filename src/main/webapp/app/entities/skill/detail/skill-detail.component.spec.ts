import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SkillDetailComponent } from './skill-detail.component';

describe('Skill Management Detail Component', () => {
  let comp: SkillDetailComponent;
  let fixture: ComponentFixture<SkillDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SkillDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./skill-detail.component').then(m => m.SkillDetailComponent),
              resolve: { skill: () => of({ id: 24455 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SkillDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SkillDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load skill on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SkillDetailComponent);

      // THEN
      expect(instance.skill()).toEqual(expect.objectContaining({ id: 24455 }));
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
