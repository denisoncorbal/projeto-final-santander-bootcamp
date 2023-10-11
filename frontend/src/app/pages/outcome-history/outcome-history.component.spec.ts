import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OutcomeHistoryComponent } from './outcome-history.component';

describe('OutcomeHistoryComponent', () => {
  let component: OutcomeHistoryComponent;
  let fixture: ComponentFixture<OutcomeHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OutcomeHistoryComponent]
    });
    fixture = TestBed.createComponent(OutcomeHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
