import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-float-message',
  templateUrl: './float-message.component.html',
  styleUrls: ['./float-message.component.css']
})
export class FloatMessageComponent {
  @Input()
  name: string = '';

  @Input()
  type: string = '';

  @Input()
  action: string = '';

}
