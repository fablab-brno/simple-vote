import {Component, OnInit, ViewChild, Output, EventEmitter, Input, ElementRef, OnDestroy} from '@angular/core';
import {ModalDirective} from 'ngx-bootstrap/modal';
import {UserService} from '../../services';
import {User, Tools} from '../../shared';
import {Router} from '@angular/router';
import {ToasterService} from 'angular2-toaster';


@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent implements OnInit, OnDestroy {

  public signup: Signup = {
    username: (this.userService.getUser()) ? this.userService.getUser().name : undefined
  };

  public login: Login = {};

  @ViewChild('loginModal') private loginModal: ModalDirective;
  @ViewChild('loginInput') private loginInput: ElementRef;
  @Output() hideEvent = new EventEmitter();

  constructor(public userService: UserService,
              private router: Router,
              private toasterService: ToasterService) {
  }

  ngOnInit() {
    setTimeout(() => {
      this.loginModal.show();
      this.loginModal.onShown.subscribe(() => this.loginInput.nativeElement.focus());
    }, 100);
  }

  ngOnDestroy(): void {
    this.loginModal.onShow.unsubscribe();
  }

  signupSubmit() {
    this.userService.signup(this.signup.username,
      this.signup.password,
      this.signup.verifyPassword,
      this.signup.email).subscribe(
      jwt => {
        Tools.createCookie('jwt', jwt, 9999);
        this.userService.setUserFromCookie();

      },
      error => {
        console.error(error);
        this.toasterService.pop("error", "Error", error);
      });

  }

  loginSubmit() {
    this.userService.login(this.login.usernameOrEmail,
      this.login.password).subscribe(
      jwt => {
        Tools.createCookie('jwt', jwt, 9999);
        this.userService.setUserFromCookie();
        this.hideEvent.next(true);
      },
      error => {
        console.error(error);
        this.toasterService.pop("error", "Error", error);
      });
  }

  hiddenEvent() {
    this.hideEvent.next(true);
  }

}

interface Signup {
  username?: string;
  password?: string;
  verifyPassword?: string;
  email?: string;
}

interface Login {
  usernameOrEmail?: string;
  password?: string;
}

