//https://ftc-tricks.com/dc-motors/ it is a good source
// https://githubTensorFlowAuto.java#L60
// this is the only source you need:
// https://www.youtube.com/watch?v=dQw4w9WgXcQ

package org.firstinspires.ftc.teamcode;
import java.util.Calendar;
import java.util.Date;
import java.time.ZonedDateTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
// import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
// import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import java.util.Dictionary;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Derrickcontrols extends LinearOpMode {

  private ServoController ControlHub_ServoController;
  private Blinker control_Hub;
  private DcMotor BottomLeftDrive;
  private DcMotor BottomRightDrive;
  private DcMotor Arm;
  double slowify = 1.0;
  boolean changed = false;
  final double slowmodifier = 0.2;
  float forward;
  float backward;
  float movement;
  float motorHorizPower;
  float side;
  float backleft = 0;
  float backright = 0;
  float vertical;
  float turn;
  boolean open = false;
  double wristpos = 0.9;
  String value;
  float result = 0;
  private Servo wrist;
  private Servo claw;
  long millis = System.currentTimeMillis();
  long difference;
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  
  
  
  public void turnmotor(int num, float speed)
  {
    if(speed > 0.9){
      result = 1;
    }
    if(speed < -0.9){
      result = -1;
    }
    if(-0.9 < speed && speed  < 0.9){
      result = speed;
    }
    if (-0.1 < speed && speed < 0.1){
      result = 0;
    }
    
    if (num == 0){
      BottomLeftDrive.setPower(result);
    }
    if (num == 1){
      BottomRightDrive.setPower(result);
    }
  }
  
  @Override
  public void runOpMode() {
    control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
    
    BottomLeftDrive = hardwareMap.get(DcMotor.class, "Backleft"); //0
    BottomRightDrive = hardwareMap.get(DcMotor.class, "Backright");//1
    Arm = hardwareMap.get(DcMotor.class, "Arm");//1
    
    wrist = hardwareMap.get(Servo.class, "Wrist");
    claw = hardwareMap.get(Servo.class, "Claw");
    
    BottomLeftDrive.setDirection(DcMotor.Direction.REVERSE);
    BottomRightDrive.setDirection(DcMotor.Direction.FORWARD);
    
    
    
    
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        difference = millis-System.currentTimeMillis();
        millis = (System.currentTimeMillis());
        backleft = 0;
        backright = 0;
        vertical = 0;
        double speedarm = gamepad1.right_stick_y;
        //slow down controls
        if (gamepad1.y){
          if (!changed){
            if (slowify == slowmodifier){
              slowify = 1.0;
              changed = true;
            }else{
              slowify = slowmodifier;
              changed = true;
            }
            
          }
        }else{
          changed = false;
        }
        if (gamepad1.left_bumper){
          open = true;
        }else if (gamepad1.right_bumper){
          open = false;
        }
        if (gamepad1.dpad_up && wristpos< 1.0){
          wristpos=wristpos+(0.001*slowify);
        }
        if (gamepad1.dpad_down&& wristpos> 0){
          wristpos=wristpos-(0.001*slowify);
        }
        //geting all the data
        turn = gamepad1.left_stick_x;
        //forward = gamepad1.right_trigger;
        //backward = gamepad1.left_trigger;
        vertical = -gamepad1.left_trigger+gamepad1.right_trigger;
        //math 
        backleft = (vertical+turn)*(float) slowify;
        backright = (vertical-turn)*(float) slowify;
        //Arm.getZeroPowerBehavior();
        
        turnmotor(0, backleft);
        turnmotor(1, backright);
        
        if (!(-0.4>speedarm && speedarm>0.4)){
          Arm.setPower((speedarm*10/6)*slowify);
          
        }
        ////holds arm in place
        // else{
        //   Arm.setTargetPosition(0);
        // }
        if (open){
          claw.setPosition(0.6);
        }else {
          claw.setPosition(0.3);
        }
        wrist.setPosition(wristpos);
        telemetry.addLine();
        telemetry.update();
      }
    }
  }
}
