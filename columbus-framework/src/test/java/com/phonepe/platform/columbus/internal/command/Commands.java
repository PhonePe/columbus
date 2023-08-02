package com.phonepe.platform.columbus.internal.command;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.impl.CommandExecutor;
import com.phonepe.platform.columbus.command.impl.CommandHandler;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;

interface Commands {

    @Data
    @Builder
    class Save implements Command<Boolean> {

        private final String contents;

        @Override
        public String name() {
            return "SAVE";
        }
    }

    @Data
    @Builder
    class Copy implements Command<String> {

        @Override
        public String name() {
            return "COPY";
        }
    }

    @Getter
    @SuperBuilder(toBuilder = true)
    class Storage extends CommandExecutor<Storage> {

        private final Consumer<String> saver;

        @Override
        protected @NonNull Storage self() {
            return this;
        }
    }

    class SaveHandler implements CommandHandler<Boolean, Save, Storage> {

        @Override
        public Boolean handle(final Save command, final Storage executor) {
            try {
                executor.getSaver().accept(command.getContents());
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }
}
