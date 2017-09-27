# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                            bigserial not null,
  answer                        varchar(255),
  valid                         boolean,
  ordem                         integer,
  pontos                        integer,
  question_id                   bigint,
  constraint pk_answer primary key (id)
);

create table campus (
  id                            bigserial not null,
  nome                          varchar(255),
  constraint pk_campus primary key (id)
);

create table curso (
  id                            bigserial not null,
  nome                          varchar(255),
  campus_id                     bigint,
  diretoria_id                  bigint,
  constraint pk_curso primary key (id)
);

create table diretoria (
  id                            bigserial not null,
  nome                          varchar(255),
  campus_id                     bigint,
  constraint pk_diretoria primary key (id)
);

create table eixo (
  id                            bigserial not null,
  nome                          varchar(255),
  constraint pk_eixo primary key (id)
);

create table pontuacao_evasao (
  id                            bigserial not null,
  pontuacao_obtida              integer,
  data_cadastro                 date,
  usuario_id                    bigint,
  resposta_id                   bigint,
  pergunta_id                   bigint,
  eixo_id                       bigint,
  quiz_id                       bigint,
  constraint pk_pontuacao_evasao primary key (id)
);

create table questao (
  id                            bigserial not null,
  pergunta                      varchar(255),
  usuario_id                    bigint,
  eixo_id                       bigint,
  quiz_id                       bigint,
  constraint pk_questao primary key (id)
);

create table quiz (
  id                            bigserial not null,
  name                          varchar(255),
  active                        boolean,
  pop_counter                   integer,
  difficulty                    integer,
  start                         timestamp,
  fim                           timestamp,
  creator_id                    bigint,
  category                      varchar(255),
  constraint pk_quiz primary key (id)
);

create table resultado (
  id                            bigserial not null,
  valor                         integer,
  opcoes_escolhidas             varchar(255),
  valor_eixo_individual         integer,
  valor_eixo_familiar           integer,
  valor_eixo_intra_escolar      integer,
  valor_eixo_carreira_profissional integer,
  valor_eixo_area_formacao      integer,
  valor_eixo_institucional      integer,
  valor_faixa_etaria15a19       integer,
  valor_faixa_etaria20a24       integer,
  valor_faixa_etaria25a29       integer,
  valor_faixa_etaria30a34       integer,
  valor_faixa_etaria35a39       integer,
  valor_faixa_etaria40ou_mais   integer,
  solteiro                      integer,
  casado                        integer,
  divorciado                    integer,
  separado                      integer,
  viuvo                         integer,
  branca                        integer,
  preta                         integer,
  parda                         integer,
  amarela                       integer,
  indigina                      integer,
  sem_declaracao                integer,
  ate1salario                   integer,
  ate3salarios                  integer,
  ate6salarios                  integer,
  ate9salarios                  integer,
  ate10ou_mais                  integer,
  masculino                     integer,
  feminino                      integer,
  valor_compara                 integer,
  opcoes_escolhidas_compara     varchar(255),
  valor_eixo_individual_compara integer,
  valor_eixo_familiar_compara   integer,
  valor_eixo_intra_escolar_compara integer,
  valor_eixo_carreira_profissional_compara integer,
  valor_eixo_area_formacao_compara integer,
  valor_eixo_institucional_compara integer,
  valor_faixa_etaria15a19compara integer,
  valor_faixa_etaria20a24compara integer,
  valor_faixa_etaria25a29compara integer,
  valor_faixa_etaria30a34compara integer,
  valor_faixa_etaria35a39compara integer,
  valor_faixa_etaria40ou_mais_compara integer,
  solteiro_compara              integer,
  casado_compara                integer,
  divorciado_compara            integer,
  separado_compara              integer,
  viuvo_compara                 integer,
  branca_compara                integer,
  preta_compara                 integer,
  parda_compara                 integer,
  amarela_compara               integer,
  indigina_compara              integer,
  sem_declaracao_compara        integer,
  ate1salario_compara           integer,
  ate3salarios_compara          integer,
  ate6salarios_compara          integer,
  ate9salarios_compara          integer,
  ate10ou_mais_compara          integer,
  masculino_compara             integer,
  feminino_compara              integer,
  constraint pk_resultado primary key (id)
);

create table solution (
  id                            bigserial not null,
  first_time                    boolean,
  first_question_show           boolean,
  done                          boolean,
  quiz_answer_time              bigint,
  best_time                     bigint,
  points                        integer,
  current_question              integer,
  start                         timestamp,
  codigo_habilitacao_nerd       integer,
  quiz_participant_id           bigint,
  quiz_id                       bigint,
  paused                        boolean,
  answered_correclty            integer,
  constraint pk_solution primary key (id)
);

create table solution_questao (
  solution_id                   bigint not null,
  questao_id                    bigint not null,
  constraint pk_solution_questao primary key (solution_id,questao_id)
);

create table turma (
  id                            bigserial not null,
  nome                          varchar(255),
  campus_id                     bigint,
  diretoria_id                  bigint,
  curso_id                      bigint,
  constraint pk_turma primary key (id)
);

create table usuario (
  id                            bigserial not null,
  nome                          varchar(255),
  matricula                     varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  is_administrador              boolean,
  is_supervisor                 boolean,
  chave_redefinicao_senha       varchar(255),
  ativo                         boolean,
  entrou                        varchar(255),
  masculino                     boolean,
  feminino                      boolean,
  faixa_etaria                  integer,
  estado_civil                  integer,
  raca                          integer,
  renda                         integer,
  telefone                      varchar(255),
  campus_id                     bigint,
  diretoria_id                  bigint,
  curso_id                      bigint,
  turma_id                      bigint,
  constraint uq_usuario_email unique (email),
  constraint pk_usuario primary key (id)
);

alter table answer add constraint fk_answer_question_id foreign key (question_id) references questao (id) on delete restrict on update restrict;
create index ix_answer_question_id on answer (question_id);

alter table curso add constraint fk_curso_campus_id foreign key (campus_id) references campus (id) on delete restrict on update restrict;
create index ix_curso_campus_id on curso (campus_id);

alter table curso add constraint fk_curso_diretoria_id foreign key (diretoria_id) references diretoria (id) on delete restrict on update restrict;
create index ix_curso_diretoria_id on curso (diretoria_id);

alter table diretoria add constraint fk_diretoria_campus_id foreign key (campus_id) references campus (id) on delete restrict on update restrict;
create index ix_diretoria_campus_id on diretoria (campus_id);

alter table pontuacao_evasao add constraint fk_pontuacao_evasao_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_pontuacao_evasao_usuario_id on pontuacao_evasao (usuario_id);

alter table pontuacao_evasao add constraint fk_pontuacao_evasao_resposta_id foreign key (resposta_id) references answer (id) on delete restrict on update restrict;
create index ix_pontuacao_evasao_resposta_id on pontuacao_evasao (resposta_id);

alter table pontuacao_evasao add constraint fk_pontuacao_evasao_pergunta_id foreign key (pergunta_id) references questao (id) on delete restrict on update restrict;
create index ix_pontuacao_evasao_pergunta_id on pontuacao_evasao (pergunta_id);

alter table pontuacao_evasao add constraint fk_pontuacao_evasao_eixo_id foreign key (eixo_id) references eixo (id) on delete restrict on update restrict;
create index ix_pontuacao_evasao_eixo_id on pontuacao_evasao (eixo_id);

alter table pontuacao_evasao add constraint fk_pontuacao_evasao_quiz_id foreign key (quiz_id) references quiz (id) on delete restrict on update restrict;
create index ix_pontuacao_evasao_quiz_id on pontuacao_evasao (quiz_id);

alter table questao add constraint fk_questao_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_questao_usuario_id on questao (usuario_id);

alter table questao add constraint fk_questao_eixo_id foreign key (eixo_id) references eixo (id) on delete restrict on update restrict;
create index ix_questao_eixo_id on questao (eixo_id);

alter table questao add constraint fk_questao_quiz_id foreign key (quiz_id) references quiz (id) on delete restrict on update restrict;
create index ix_questao_quiz_id on questao (quiz_id);

alter table quiz add constraint fk_quiz_creator_id foreign key (creator_id) references usuario (id) on delete restrict on update restrict;
create index ix_quiz_creator_id on quiz (creator_id);

alter table solution add constraint fk_solution_quiz_participant_id foreign key (quiz_participant_id) references usuario (id) on delete restrict on update restrict;
create index ix_solution_quiz_participant_id on solution (quiz_participant_id);

alter table solution add constraint fk_solution_quiz_id foreign key (quiz_id) references quiz (id) on delete restrict on update restrict;
create index ix_solution_quiz_id on solution (quiz_id);

alter table solution_questao add constraint fk_solution_questao_solution foreign key (solution_id) references solution (id) on delete restrict on update restrict;
create index ix_solution_questao_solution on solution_questao (solution_id);

alter table solution_questao add constraint fk_solution_questao_questao foreign key (questao_id) references questao (id) on delete restrict on update restrict;
create index ix_solution_questao_questao on solution_questao (questao_id);

alter table turma add constraint fk_turma_campus_id foreign key (campus_id) references campus (id) on delete restrict on update restrict;
create index ix_turma_campus_id on turma (campus_id);

alter table turma add constraint fk_turma_diretoria_id foreign key (diretoria_id) references diretoria (id) on delete restrict on update restrict;
create index ix_turma_diretoria_id on turma (diretoria_id);

alter table turma add constraint fk_turma_curso_id foreign key (curso_id) references curso (id) on delete restrict on update restrict;
create index ix_turma_curso_id on turma (curso_id);

alter table usuario add constraint fk_usuario_campus_id foreign key (campus_id) references campus (id) on delete restrict on update restrict;
create index ix_usuario_campus_id on usuario (campus_id);

alter table usuario add constraint fk_usuario_diretoria_id foreign key (diretoria_id) references diretoria (id) on delete restrict on update restrict;
create index ix_usuario_diretoria_id on usuario (diretoria_id);

alter table usuario add constraint fk_usuario_curso_id foreign key (curso_id) references curso (id) on delete restrict on update restrict;
create index ix_usuario_curso_id on usuario (curso_id);

alter table usuario add constraint fk_usuario_turma_id foreign key (turma_id) references turma (id) on delete restrict on update restrict;
create index ix_usuario_turma_id on usuario (turma_id);


# --- !Downs

alter table if exists answer drop constraint if exists fk_answer_question_id;
drop index if exists ix_answer_question_id;

alter table if exists curso drop constraint if exists fk_curso_campus_id;
drop index if exists ix_curso_campus_id;

alter table if exists curso drop constraint if exists fk_curso_diretoria_id;
drop index if exists ix_curso_diretoria_id;

alter table if exists diretoria drop constraint if exists fk_diretoria_campus_id;
drop index if exists ix_diretoria_campus_id;

alter table if exists pontuacao_evasao drop constraint if exists fk_pontuacao_evasao_usuario_id;
drop index if exists ix_pontuacao_evasao_usuario_id;

alter table if exists pontuacao_evasao drop constraint if exists fk_pontuacao_evasao_resposta_id;
drop index if exists ix_pontuacao_evasao_resposta_id;

alter table if exists pontuacao_evasao drop constraint if exists fk_pontuacao_evasao_pergunta_id;
drop index if exists ix_pontuacao_evasao_pergunta_id;

alter table if exists pontuacao_evasao drop constraint if exists fk_pontuacao_evasao_eixo_id;
drop index if exists ix_pontuacao_evasao_eixo_id;

alter table if exists pontuacao_evasao drop constraint if exists fk_pontuacao_evasao_quiz_id;
drop index if exists ix_pontuacao_evasao_quiz_id;

alter table if exists questao drop constraint if exists fk_questao_usuario_id;
drop index if exists ix_questao_usuario_id;

alter table if exists questao drop constraint if exists fk_questao_eixo_id;
drop index if exists ix_questao_eixo_id;

alter table if exists questao drop constraint if exists fk_questao_quiz_id;
drop index if exists ix_questao_quiz_id;

alter table if exists quiz drop constraint if exists fk_quiz_creator_id;
drop index if exists ix_quiz_creator_id;

alter table if exists solution drop constraint if exists fk_solution_quiz_participant_id;
drop index if exists ix_solution_quiz_participant_id;

alter table if exists solution drop constraint if exists fk_solution_quiz_id;
drop index if exists ix_solution_quiz_id;

alter table if exists solution_questao drop constraint if exists fk_solution_questao_solution;
drop index if exists ix_solution_questao_solution;

alter table if exists solution_questao drop constraint if exists fk_solution_questao_questao;
drop index if exists ix_solution_questao_questao;

alter table if exists turma drop constraint if exists fk_turma_campus_id;
drop index if exists ix_turma_campus_id;

alter table if exists turma drop constraint if exists fk_turma_diretoria_id;
drop index if exists ix_turma_diretoria_id;

alter table if exists turma drop constraint if exists fk_turma_curso_id;
drop index if exists ix_turma_curso_id;

alter table if exists usuario drop constraint if exists fk_usuario_campus_id;
drop index if exists ix_usuario_campus_id;

alter table if exists usuario drop constraint if exists fk_usuario_diretoria_id;
drop index if exists ix_usuario_diretoria_id;

alter table if exists usuario drop constraint if exists fk_usuario_curso_id;
drop index if exists ix_usuario_curso_id;

alter table if exists usuario drop constraint if exists fk_usuario_turma_id;
drop index if exists ix_usuario_turma_id;

drop table if exists answer cascade;

drop table if exists campus cascade;

drop table if exists curso cascade;

drop table if exists diretoria cascade;

drop table if exists eixo cascade;

drop table if exists pontuacao_evasao cascade;

drop table if exists questao cascade;

drop table if exists quiz cascade;

drop table if exists resultado cascade;

drop table if exists solution cascade;

drop table if exists solution_questao cascade;

drop table if exists turma cascade;

drop table if exists usuario cascade;

