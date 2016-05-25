package br.uern;

import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class Main {
	private static final int MAX_COUNT = 2147483647;
	private static final double VR_MAX = 0.203;
	private static final double RAIO = 0.021; // raio da roda 0.028m ou 2.8cm
	private static final double EIXO = 0.14; // tamanho do eixo entre rodas
	private static final int REV = 360;

	private static long pulsosEsq, pulsosEsqAnt;
	private static long pulsosDir, pulsosDirAnt;
	private static long NPe;
	private static long NPd;
	private static double deltaThetaEsq, deltaThetaDir;
	private static double deltaSEsq, deltaSDir;
	private static double deltaS, deltaTheta;

	// variáveis da pose
	static double x;
	static double y;
	static double th;
	static double x_ant;
	static double y_ant;
	static double th_ant;

	// inicialização das velocidades
	static double v = 0.05; // velocidade linear
	static double w = 0.0; // velocidade angular
	static double delta = 0.1; // variável de tempo
	static double velEsq = 0; // velocidade do motor esquerdo
	static double velDir = 0; // velociddade do motor direito

	public static void main(String[] args) {
		// inicialização da pose
		x = 0.0;
		y = 0.0;
		th = 0.0;
		x_ant = 0.0;
		y_ant = 0.0;
		th_ant = 0.0;

		// primeiras leituras
		pulsosEsqAnt = Motor.A.getTachoCount(); // MotorRotationCount(MOTOR_ESQ)
		pulsosDirAnt = Motor.C.getTachoCount(); // MotorRotationCount(MOTOR_DIR);

		while (x_ant <= 0.3) {

			// coloque os cálculos de velocidade aqui
			velDir = ((2.0 * v + w * EIXO) * 100) / (VR_MAX * 2.0);
			velEsq = ((2.0 * v - w * EIXO) * 100) / (VR_MAX * 2.0);

			Motor.A.setSpeed((int) velDir);
			Motor.C.setSpeed((int) velEsq);

			// liga os motores
			Motor.A.forward(); // OnFwd(OUT_A,velDir);
			Motor.C.forward(); // OnFwd(OUT_B,velEsq);

			// ciclo de operação de 100ms
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// leituras atuais do encoder
			pulsosEsq = Motor.A.getTachoCount();
			pulsosDir = Motor.C.getTachoCount();

			// quantidade de pulsos durante um intervalo Dt
			NPe = pulsosEsq - pulsosEsqAnt;
			NPd = pulsosDir - pulsosDirAnt;

			// tratamento de descontinuidades encoder esquerdo
			if (Math.abs(NPe) > MAX_COUNT / 2) {
				if (NPe < 0) {
					NPe = MAX_COUNT + NPe;
				} else {
					NPe = -MAX_COUNT + NPe;
				}
			}

			// tratamento de descontinuidades encoder direito
			if (Math.abs(NPd) > MAX_COUNT / 2) {
				if (NPd < 0) {
					NPd = MAX_COUNT + NPd;
				} else {
					NPd = -MAX_COUNT + NPd;
				}
			}

			// equacoes de odometria ou de leitura de encoders
			deltaThetaEsq = (2 * Math.PI * NPe) / REV;
			deltaThetaDir = (2 * Math.PI * NPd) / REV;

			deltaSEsq = RAIO * deltaThetaEsq;
			deltaSDir = RAIO * deltaThetaDir;

			deltaS = (deltaSDir + deltaSEsq) / 2;
			deltaTheta = (deltaSDir - deltaSEsq) / EIXO;

			x = x_ant + deltaS * Math.cos(th_ant);
			y = y_ant + deltaS * Math.sin(th_ant);
			th = th_ant + deltaTheta;

			// atualizando dados
			x_ant = x;
			y_ant = y;
			th_ant = th;
			pulsosEsqAnt = pulsosEsq;
			pulsosDirAnt = pulsosDir;

			// show
			LCD.drawString(String.valueOf(x), 10, 30);
			LCD.drawString(String.valueOf(y), 10, 20);
			LCD.drawString(String.valueOf(th), 10, 10);

		}

		Motor.A.stop();
		Motor.C.stop();

	}

}
