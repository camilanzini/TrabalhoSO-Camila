package app;

import java.util.Scanner;

import java.util.Random;

public class App {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;

    static int n_processos = 3;  
     
    public static void main(String[] args) {

     int[] tempo_execucao = new int[n_processos];
     int[] tempo_chegada = new int[n_processos];
     int[] prioridade = new int[n_processos];
     int[] tempo_espera = new int[n_processos];
     int[] tempo_restante = new int[n_processos];
     
      
      Scanner teclado = new Scanner (System.in);
      
      
      popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      
      imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      
      //Escolher algoritmo
      int alg;
      
      while(true) {
        System.out.print("Escolha o argoritmo?: [1=FCFS 2=SJF Preemptivo 3=SJF Não Preemptivo  4=Prioridade Preemptivo 5=Prioridade Não Preemptivo  6=Round_Robin  7=Imprime lista de processos 8=Popular processos novamente 9=Sair]: ");
        alg =  teclado.nextInt();
        
        
        if (alg == 1) { //FCFS
            FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        }
        else if (alg == 2) { //SJF PREEMPTIVO
            SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        }
        else if (alg == 3) { //SJF NÃO PREEMPTIVO
            SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            
        }
        else if (alg == 4) { //PRIORIDADE PREEMPTIVO
            PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }
        else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
        	PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            
        }
        else if (alg == 6) { //Round_Robin
        	Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
            
        }
        else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
        	imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }

        else if (alg == 8) { //REATRIBUI VALORES INICIAIS
            popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }
        else if (alg == 9) {
            break;
            
        }
    }
              
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;

        System.out.print("Será aleatório?:  ");
        aleatorio =  teclado.nextInt();

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1;
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];
    
          }
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int []prioridade){
        //Imprime lista de processos
      for (int i = 0; i < n_processos; i++) {
        System.out.println("Processo["+i+"]: tempo_execucao="+ tempo_execucao[i] + " tempo_restante="+tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" +prioridade[i]);
    }
    }

    public static void imprime_stats (int[] espera) {
        int[] tempo_espera = espera.clone();
        //Implementar o calculo e impressão de estatisticas
        
        double tempo_espera_total = 0;
        
        for(int i=0; i<n_processos; i++){ 
            System.out.println("Processo["+i+"]: tempo_espera="+tempo_espera[i]);
            tempo_espera_total = tempo_espera_total + tempo_espera[i];
        }

        System.out.println("Tempo médio de espera: "+(tempo_espera_total/n_processos));
        
    }
    
    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        //int[] tempo_chegada = chegada.clone();

        int processo_em_execucao = 0; //processo inicial no FIFO é o zero

        //implementar código do FCFS
        for (int i=1; i<MAXIMO_TEMPO_EXECUCAO; i++) {
            System.out.println("tempo["+i+"]: processo["+processo_em_execucao+"] restante="+tempo_restante[processo_em_execucao]);
            
            if (tempo_execucao[processo_em_execucao] == tempo_restante[processo_em_execucao])
                tempo_espera[processo_em_execucao] = i-1;

            if (tempo_restante[processo_em_execucao] == 1) {
                if (processo_em_execucao == (n_processos-1))
                    break;
                else
                    processo_em_execucao++;
            }
            else
                tempo_restante[processo_em_execucao]--;
            
        }
        //

        imprime_stats(tempo_espera);
    }
    
    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        //implementar código do SJF preemptivo e não preemptivo
        if (preemptivo) {
            SJF_Preemptivo(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        } else {
            SJF_NaoPreemptivo(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        }
    }
    
    // Método para imprimir a linha do tempo de execução dos processos
public static void imprimirLinhaDoTempo(int[] ordem_execucao, int[] tempo_restante) {
    System.out.print("Linha do tempo de execução dos processos: ");
    for (int i = 0; i < ordem_execucao.length; i++) {
        if (ordem_execucao[i] != -1) {
            System.out.print("tempo[" + (i + 1) + "]: processo[" + ordem_execucao[i] + "] restante="
                    + tempo_restante[ordem_execucao[i]]);
            if (i < ordem_execucao.length - 1) {
                System.out.print(", ");
            }
        }
    }
    System.out.println();
}

// Método SJF_Preemptivo
public static void SJF_Preemptivo(int[] execucao, int[] espera, int[] restante, int[] chegada) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int[] ordem_execucao = new int[MAXIMO_TEMPO_EXECUCAO];
    int tempo_atual = 0;

    // Inicializa a ordem de execução com -1 (indicando que nenhum processo foi executado nesse momento)
    Arrays.fill(ordem_execucao, -1);

    while (true) {
        int menorTempoExecucao = Integer.MAX_VALUE;
        int proximoProcesso = -1;

        // Encontra o próximo processo a ser executado
        for (int i = 0; i < n_processos; i++) {
            if (tempo_chegada[i] <= tempo_atual && tempo_restante[i] < menorTempoExecucao && tempo_restante[i] > 0) {
                menorTempoExecucao = tempo_restante[i];
                proximoProcesso = i;
            }
        }

        // Caso não haja mais processos a serem executados
        if (proximoProcesso == -1) {
            break;
        }

        // Executa o próximo processo
        ordem_execucao[tempo_atual] = proximoProcesso;
        tempo_restante[proximoProcesso]--;
        tempo_atual++;
        tempo_espera[proximoProcesso]++;

        // Verifica se o processo atual foi concluído
        if (tempo_restante[proximoProcesso] == 0) {
            tempo_espera[proximoProcesso] -= tempo_execucao[proximoProcesso];
        }

        // Imprime a linha do tempo de execução dos processos a cada ciclo
        imprimirLinhaDoTempo(ordem_execucao, tempo_restante);
    }

    // Imprime as estatísticas
    imprime_stats(tempo_espera);
}

// Método SJF_NaoPreemptivo
public static void SJF_NaoPreemptivo(int[] execucao, int[] espera, int[] restante, int[] chegada) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int[] ordem_execucao = new int[MAXIMO_TEMPO_EXECUCAO];
    int tempo_atual = 0;

    // Inicializa a ordem de execução com -1 (indicando que nenhum processo foi executado nesse momento)
    Arrays.fill(ordem_execucao, -1);

    while (true) {
        int menorTempoExecucao = Integer.MAX_VALUE;
        int proximoProcesso = -1;

        // Encontra o próximo processo a ser executado
        for (int i = 0; i < n_processos; i++) {
            if (tempo_chegada[i] <= tempo_atual && tempo_restante[i] < menorTempoExecucao && tempo_restante[i] > 0) {
                menorTempoExecucao = tempo_restante[i];
                proximoProcesso = i;
            }
        }

        // Caso não haja mais processos a serem executados
        if (proximoProcesso == -1) {
            break;
        }

        // Executa o próximo processo
        ordem_execucao[tempo_atual] = proximoProcesso;
        tempo_restante[proximoProcesso]--;
        tempo_atual++;
        tempo_espera[proximoProcesso]++;

        // Verifica se o processo atual foi concluído
        if (tempo_restante[proximoProcesso] == 0) {
            tempo_espera[proximoProcesso] -= tempo_execucao[proximoProcesso];
        }

        // Imprime a linha do tempo de execução dos processos a cada ciclo
        imprimirLinhaDoTempo(ordem_execucao, tempo_restante);
    }

    // Imprime as estatísticas
    imprime_stats(tempo_espera);
}

    

public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int[] prioridade_temp = prioridade.clone();

    if (preemptivo) {
        int tempo_atual = 0;
        int processo_em_execucao = -1;
        while (true) {
            int maiorPrioridade = Integer.MAX_VALUE;
            int proximoProcesso = -1;

            for (int i = 0; i < n_processos; i++) {
                if (tempo_chegada[i] <= tempo_atual && prioridade_temp[i] < maiorPrioridade && tempo_restante[i] > 0) {
                    maiorPrioridade = prioridade_temp[i];
                    proximoProcesso = i;
                }
            }

            if (proximoProcesso == -1) {
                break;
            }

            if (proximoProcesso != processo_em_execucao) {
                if (processo_em_execucao != -1) {
                    tempo_espera[processo_em_execucao] += tempo_atual;
                }
                processo_em_execucao = proximoProcesso;
            }

            tempo_restante[proximoProcesso]--;
            tempo_atual++;

            if (tempo_restante[proximoProcesso] == 0) {
                tempo_espera[proximoProcesso] -= tempo_execucao[proximoProcesso];
                processo_em_execucao = -1;
            }

            imprimirLinhaDoTempo(ordem_execucao, tempo_restante);
        }
    } else {
        for (int i = 0; i < n_processos - 1; i++) {
            for (int j = 0; j < n_processos - i - 1; j++) {
                if (prioridade_temp[j] > prioridade_temp[j + 1]) {
                    int temp = prioridade_temp[j];
                    prioridade_temp[j] = prioridade_temp[j + 1];
                    prioridade_temp[j + 1] = temp;

                    temp = tempo_execucao[j];
                    tempo_execucao[j] = tempo_execucao[j + 1];
                    tempo_execucao[j + 1] = temp;

                    temp = tempo_chegada[j];
                    tempo_chegada[j] = tempo_chegada[j + 1];
                    tempo_chegada[j + 1] = temp;

                    temp = tempo_restante[j];
                    tempo_restante[j] = tempo_restante[j + 1];
                    tempo_restante[j + 1] = temp;
                }
            }
        }

        int tempo_atual = 0;
        for (int i = 0; i < n_processos; i++) {
            tempo_espera[i] = tempo_atual - tempo_chegada[i];
            tempo_atual += tempo_execucao[i];
        }
    }

    imprime_stats(tempo_espera);
}

    
    public static void Round_Robin(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        
        //implementar código do Round-Robin
        
        int quantum = 2; // Defina o valor do quantum conforme necessário

        // Implementação do algoritmo Round Robin
        int tempo_atual = 0;
        int[] ordem_execucao = new int[MAXIMO_TEMPO_EXECUCAO];
        Arrays.fill(ordem_execucao, -1);
        int index = 0;
    
        while (true) {
            boolean todosProcessosConcluidos = true;
            for (int i = 0; i < n_processos; i++) {
                if (tempo_restante[i] > 0) {
                    todosProcessosConcluidos = false;
    
                    // Executa o processo atual
                    if (tempo_restante[i] > quantum) {
                        tempo_atual += quantum;
                        tempo_restante[i] -= quantum;
                    } else {
                        tempo_atual += tempo_restante[i];
                        tempo_espera[i] = tempo_atual - tempo_execucao[i] - tempo_espera[i];
                        tempo_restante[i] = 0;
                    }
                    ordem_execucao[index++] = i;
    
                    // Imprime a linha do tempo de execução dos processos a cada ciclo
                    imprimirLinhaDoTempo(ordem_execucao, tempo_restante);
                }
            }
    
            // Verifica se todos os processos foram concluídos
            if (todosProcessosConcluidos) {
                break;
            }
        }
    
        // Imprime as estatísticas
        imprime_stats(tempo_espera);
    }
}

