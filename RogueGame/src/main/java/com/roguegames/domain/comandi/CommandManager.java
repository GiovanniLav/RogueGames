package com.roguegames.domain.comandi;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> history = new ArrayList<>();

    public void eseguiComando(Command comando) {
        comando.execute();
        history.add(comando); // Salva nel registro per possibile undo
    }

    public List<Command> getStoricoComandi() {
        return history;
    }
}
