import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPerfilUsuario } from 'app/entities/perfil-usuario/perfil-usuario.model';
import { PerfilUsuarioService } from 'app/entities/perfil-usuario/service/perfil-usuario.service';
import { SkillService } from '../service/skill.service';
import { ISkill } from '../skill.model';
import { SkillFormService } from './skill-form.service';

import { SkillUpdateComponent } from './skill-update.component';

describe('Skill Management Update Component', () => {
  let comp: SkillUpdateComponent;
  let fixture: ComponentFixture<SkillUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let skillFormService: SkillFormService;
  let skillService: SkillService;
  let perfilUsuarioService: PerfilUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SkillUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SkillUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SkillUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    skillFormService = TestBed.inject(SkillFormService);
    skillService = TestBed.inject(SkillService);
    perfilUsuarioService = TestBed.inject(PerfilUsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PerfilUsuario query and add missing value', () => {
      const skill: ISkill = { id: 21768 };
      const perfilUsuarios: IPerfilUsuario[] = [{ id: 8289 }];
      skill.perfilUsuarios = perfilUsuarios;

      const perfilUsuarioCollection: IPerfilUsuario[] = [{ id: 8289 }];
      jest.spyOn(perfilUsuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilUsuarioCollection })));
      const additionalPerfilUsuarios = [...perfilUsuarios];
      const expectedCollection: IPerfilUsuario[] = [...additionalPerfilUsuarios, ...perfilUsuarioCollection];
      jest.spyOn(perfilUsuarioService, 'addPerfilUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ skill });
      comp.ngOnInit();

      expect(perfilUsuarioService.query).toHaveBeenCalled();
      expect(perfilUsuarioService.addPerfilUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        perfilUsuarioCollection,
        ...additionalPerfilUsuarios.map(expect.objectContaining),
      );
      expect(comp.perfilUsuariosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const skill: ISkill = { id: 21768 };
      const perfilUsuario: IPerfilUsuario = { id: 8289 };
      skill.perfilUsuarios = [perfilUsuario];

      activatedRoute.data = of({ skill });
      comp.ngOnInit();

      expect(comp.perfilUsuariosSharedCollection).toContainEqual(perfilUsuario);
      expect(comp.skill).toEqual(skill);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkill>>();
      const skill = { id: 24455 };
      jest.spyOn(skillFormService, 'getSkill').mockReturnValue(skill);
      jest.spyOn(skillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skill }));
      saveSubject.complete();

      // THEN
      expect(skillFormService.getSkill).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(skillService.update).toHaveBeenCalledWith(expect.objectContaining(skill));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkill>>();
      const skill = { id: 24455 };
      jest.spyOn(skillFormService, 'getSkill').mockReturnValue({ id: null });
      jest.spyOn(skillService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skill: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skill }));
      saveSubject.complete();

      // THEN
      expect(skillFormService.getSkill).toHaveBeenCalled();
      expect(skillService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkill>>();
      const skill = { id: 24455 };
      jest.spyOn(skillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(skillService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePerfilUsuario', () => {
      it('should forward to perfilUsuarioService', () => {
        const entity = { id: 8289 };
        const entity2 = { id: 13667 };
        jest.spyOn(perfilUsuarioService, 'comparePerfilUsuario');
        comp.comparePerfilUsuario(entity, entity2);
        expect(perfilUsuarioService.comparePerfilUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
