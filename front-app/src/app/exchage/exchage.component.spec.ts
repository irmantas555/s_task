import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchageComponent } from './exchage.component';

describe('ExchageComponent', () => {
  let component: ExchageComponent;
  let fixture: ComponentFixture<ExchageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExchageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
