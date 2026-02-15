import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';
import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioFormService } from './perfil-usuario-form.service';

import { PerfilUsuarioUpdateComponent } from './perfil-usuario-update.component';

describe('PerfilUsuario Management Update Component', () => {
  let comp: PerfilUsuarioUpdateComponent;
  let fixture: ComponentFixture<PerfilUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let perfilUsuarioFormService: PerfilUsuarioFormService;
  let perfilUsuarioService: PerfilUsuarioService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PerfilUsuarioUpdateComponent],
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
      .overrideTemplate(PerfilUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    perfilUsuarioFormService = TestBed.inject(PerfilUsuarioFormService);
    perfilUsuarioService = TestBed.inject(PerfilUsuarioService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const perfilUsuario: IPerfilUsuario = { id: 13667 };
      const user: IUser = { id: 3944 };
      perfilUsuario.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const perfilUsuario: IPerfilUsuario = { id: 13667 };
      const user: IUser = { id: 3944 };
      perfilUsuario.user = user;

      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.perfilUsuario).toEqual(perfilUsuario);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 8289 };
      jest.spyOn(perfilUsuarioFormService, 'getPerfilUsuario').mockReturnValue(perfilUsuario);
      jest.spyOn(perfilUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilUsuario }));
      saveSubject.complete();

      // THEN
      expect(perfilUsuarioFormService.getPerfilUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(perfilUsuarioService.update).toHaveBeenCalledWith(expect.objectContaining(perfilUsuario));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 8289 };
      jest.spyOn(perfilUsuarioFormService, 'getPerfilUsuario').mockReturnValue({ id: null });
      jest.spyOn(perfilUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilUsuario }));
      saveSubject.complete();

      // THEN
      expect(perfilUsuarioFormService.getPerfilUsuario).toHaveBeenCalled();
      expect(perfilUsuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 8289 };
      jest.spyOn(perfilUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(perfilUsuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
