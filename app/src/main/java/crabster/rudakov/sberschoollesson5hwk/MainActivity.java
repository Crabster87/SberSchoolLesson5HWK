package crabster.rudakov.sberschoollesson5hwk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ArtificialIntelligence intelligence;
    private int countOfRounds = 1;
    private int countOfPlayersVictories = 0;
    private int countOfAIVictories = 0;
    private ImageView field_00;
    private ImageView field_01;
    private ImageView field_02;
    private ImageView field_10;
    private ImageView field_11;
    private ImageView field_12;
    private ImageView field_20;
    private ImageView field_21;
    private ImageView field_22;
    private TextView roundsCount;
    private TextView playerWinsCount;
    private TextView aIWinsCount;
    private ImageView imagePlayerWinner;
    private int playerWinnerLevel = 0;
    private ImageView imageAIWinner;
    private int aIWinnerLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roundsCount = findViewById(R.id.rounds_count);
        roundsCount.setText(String.valueOf(countOfRounds));
        playerWinsCount = findViewById(R.id.player_wins_count);
        playerWinsCount.setText(String.valueOf(countOfPlayersVictories));
        aIWinsCount = findViewById(R.id.ai_wins_count);
        aIWinsCount.setText(String.valueOf(countOfAIVictories));

        field_00 = findViewById(R.id.field_00);
        field_01 = findViewById(R.id.field_01);
        field_02 = findViewById(R.id.field_02);
        field_10 = findViewById(R.id.field_10);
        field_11 = findViewById(R.id.field_11);
        field_12 = findViewById(R.id.field_12);
        field_20 = findViewById(R.id.field_20);
        field_21 = findViewById(R.id.field_21);
        field_22 = findViewById(R.id.field_22);

        intelligence = new ArtificialIntelligence();
        intelligence.bindFieldsAndViews(R.id.field_00, R.id.field_01, R.id.field_02,
                                        R.id.field_10, R.id.field_11, R.id.field_12,
                                        R.id.field_20, R.id.field_21, R.id.field_22);

        setListeners();
        setScoreLevels();

        AppCompatButton nextRoundButton = findViewById(R.id.next_round_button);
        nextRoundButton.setOnClickListener(v -> refreshGameField());
    }

    /**
     * Метод устанавливает слушателей на каждую ячейку игрового поля. По нажатию происходит
     * ход игрока и ответный ход AI.
     * */
    private void setListeners() {
        field_00.setOnClickListener(v -> {
            makePlayerStep(0, 0, field_00);
            makeAIRespond();
        });
        field_01.setOnClickListener(v -> {
            makePlayerStep(0, 1, field_01);
            makeAIRespond();
        });
        field_02.setOnClickListener(v -> {
            makePlayerStep(0, 2, field_02);
            makeAIRespond();
        });
        field_10.setOnClickListener(v -> {
            makePlayerStep(1, 0, field_10);
            makeAIRespond();
        });
        field_11.setOnClickListener(v -> {
            makePlayerStep(1, 1, field_11);
            makeAIRespond();
        });
        field_12.setOnClickListener(v -> {
            makePlayerStep(1, 2, field_12);
            makeAIRespond();
        });
        field_20.setOnClickListener(v -> {
            makePlayerStep(2, 0, field_20);
            makeAIRespond();
        });
        field_21.setOnClickListener(v -> {
            makePlayerStep(2, 1, field_21);
            makeAIRespond();
        });
        field_22.setOnClickListener(v -> {
            makePlayerStep(2, 2, field_22);
            makeAIRespond();
        });
    }

    /**
     * Метод устанавливает уровни прогресса очков игрока и AI.
     * */
    private void setScoreLevels() {
        imagePlayerWinner = findViewById(R.id.image_player_winner);
        imagePlayerWinner.setImageLevel(playerWinnerLevel);
        imageAIWinner = findViewById(R.id.image_ai_winner);
        imageAIWinner.setImageLevel(aIWinnerLevel);
    }

    /**
     * Метод осуществляет ход игрока, передавая AI координаты хода игрока. Ход отмечается
     * крестиком с применением анимации, делая при этом поле недоступным. В конце метод
     * проводит проверку не является ли ход победным.
     * */
    private void makePlayerStep(int y, int x, ImageView field) {
        intelligence.setPlayersStep(y, x);
        field.setImageResource(R.drawable.cross);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_expand_from_center);
        field.startAnimation(animation);
        field.setEnabled(false);
        checkPlayerWinner();
    }

    /**
     * Метод осуществляет ответный ход AI, получая объект с некоторыми координатами и
     * учтенным статусом. Если ход был сделан (соответствующее View ячецки не равно 0),
     * то отмечает ход AI ноликом с применением анимации, делая при этом поле недоступным.
     * */
    private void makeAIRespond() {
        int aIStep = intelligence.setAIStep();
        if (aIStep != 0) {
            ImageView field = findViewById(aIStep);
            field.setImageResource(R.drawable.zero);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_expand_from_center);
            field.startAnimation(animation);
            field.setEnabled(false);
            checkAIWinner();
        }
    }

    /**
     * Метод делает невозможными ходы в игровом поле.
     * */
    private void setGameFieldEnabled() {
        field_00.setEnabled(false);
        field_01.setEnabled(false);
        field_02.setEnabled(false);
        field_10.setEnabled(false);
        field_11.setEnabled(false);
        field_12.setEnabled(false);
        field_20.setEnabled(false);
        field_21.setEnabled(false);
        field_22.setEnabled(false);
    }

    /**
     * Метод обеспечивает обновление игрового поля, очищая его и создавая новый раунд с
     * подсчитанным порядковым номером. Каждый номер раунда отмечает в соотвествующем поле.
     * Останавливает игру в случае набора кем бы то ни было 3 очков.
     * */
    private void refreshGameField() {
        countOfRounds++;
        if (countOfPlayersVictories < 3 && countOfAIVictories < 3) {
            roundsCount.setText(String.valueOf(countOfRounds));
            intelligence = new ArtificialIntelligence();
            intelligence.bindFieldsAndViews(R.id.field_00, R.id.field_01, R.id.field_02,
                    R.id.field_10, R.id.field_11, R.id.field_12,
                    R.id.field_20, R.id.field_21, R.id.field_22);
            field_00.setImageResource(0);
            field_01.setImageResource(0);
            field_02.setImageResource(0);
            field_10.setImageResource(0);
            field_11.setImageResource(0);
            field_12.setImageResource(0);
            field_20.setImageResource(0);
            field_21.setImageResource(0);
            field_22.setImageResource(0);
            field_00.setEnabled(true);
            field_01.setEnabled(true);
            field_02.setEnabled(true);
            field_10.setEnabled(true);
            field_11.setEnabled(true);
            field_12.setEnabled(true);
            field_20.setEnabled(true);
            field_21.setEnabled(true);
            field_22.setEnabled(true);
        }
    }

    /**
     * Используя данные, полученные от AI, метод оповещает игрока о победе отправкой
     * тост-сообщения и обновлением индикатора набранных очков. В конце блокирует возможность
     * дальнейших ходов.
     * */
    private void checkPlayerWinner() {
        if (intelligence.checkWin(1) && !intelligence.checkWin(2)) {
            Toast.makeText(this, getString(R.string.toast_message_for_winner), Toast.LENGTH_SHORT).show();
            countOfPlayersVictories++;
            playerWinnerLevel += 3333;
            playerWinsCount.setText(String.valueOf(countOfPlayersVictories));
            imagePlayerWinner.setImageLevel(playerWinnerLevel);
            setGameFieldEnabled();
        }
    }

    /**
     * Используя данные, полученные от AI, метод оповещает игрока о его поражении отправкой
     * тост-сообщения и обновлением индикатора набранных AI очков. В конце блокирует
     * возможность дальнейших ходов.
     * */
    private void checkAIWinner() {
        if (intelligence.checkWin(2) && !intelligence.checkWin(1)) {
            Toast.makeText(this, getString(R.string.toast_message_for_looser), Toast.LENGTH_SHORT).show();
            countOfAIVictories++;
            aIWinnerLevel += 3333;
            aIWinsCount.setText(String.valueOf(countOfAIVictories));
            imageAIWinner.setImageLevel(aIWinnerLevel);
            setGameFieldEnabled();
        }
    }

}