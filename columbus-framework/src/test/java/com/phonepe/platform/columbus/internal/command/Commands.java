package com.phonepe.platform.columbus.internal.command;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.impl.CommandExecutor;
import com.phonepe.platform.columbus.command.impl.CommandHandler;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;

interface Commands {

    interface StorageHandler<R, C extends Command<R>> extends CommandHandler<R, C, Storage> {

        @Override
        R handle(C command, Storage storage);
    }

    interface NetworkHandler<R, C extends Command<R>> extends CommandHandler<R, C, Network> {

        @Override
        R handle(C command, Network network);
    }

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

    @Getter
    @SuperBuilder(toBuilder = true)
    class Network extends CommandExecutor<Network> {

        private final Consumer<String> sender;

        @Override
        protected @NonNull Network self() {
            return this;
        }
    }

    @RegistryKey(name = "Save", type = Save.class)
    class SaveHandler implements StorageHandler<Boolean, Save> {

        @Override
        public Boolean handle(final Save command, final Storage storage) {
            try {
                storage.getSaver().accept(command.getContents());
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    @RegistryKey(type = Save.class)
    class SaveSender implements NetworkHandler<Boolean, Save> {

        @Override
        public Boolean handle(final Save command, final Network network) {
            try {
                network.getSender().accept(command.getContents());
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }
}
