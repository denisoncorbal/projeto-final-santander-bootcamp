import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FloatMessageComponent } from './float-message.component';

describe('FloatMessageComponent', () => {
  let component: FloatMessageComponent;
  let fixture: ComponentFixture<FloatMessageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FloatMessageComponent]
    });
    fixture = TestBed.createComponent(FloatMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
