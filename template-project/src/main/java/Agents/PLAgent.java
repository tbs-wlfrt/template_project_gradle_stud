package Agents;

import Layer1_Dev.dev1;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.ParallelBehaviour;



public class PLAgent extends Agent {
    @Override
    public void setup() {
        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
        addBehaviour(comportementSequentiel);
        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                if (dev1.readcolor()){
                    comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            dev1.dropMotor();
                        }
                    });
                    comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            dev1.dropdown();
                        }
                    });
                    comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                    ParallelBehaviour parallelBehaviour= new ParallelBehaviour();
                    addBehaviour(parallelBehaviour);
                    parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            dev1.startconvoyor1();
                        }
                    });
                    parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            dev1.startshrederMotor();
                        }
                    });
                        }
                    });
                    }
                else {
                        System.out.println("fffffff");
                }
                }

        });

    }}