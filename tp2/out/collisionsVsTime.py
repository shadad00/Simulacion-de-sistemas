import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
epsilon = 0.05

if __name__ == '__main__':

    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    n_list = [2000, 3000, 5000]

    for n in n_list:
        print(f'n = {n}')
        files = Path(path).glob(f'*{n}_i*aggregate.csv')
        dfs = [pd.read_csv(f) for f in files]
        df = pd.concat(dfs, ignore_index=True)

        mean = df.groupby(["iteration"])[["solid_collision", "particle_collision"]].mean()
        std = df.groupby(["iteration"])[["solid_collision", "particle_collision"]].std()

        mean["solid_collision"][::100].plot(yerr=std["solid_collision"][::100])
        plt.xlabel("Número de iteración")
        plt.ylabel("Cantidad de colisiones contra sólidas")
        plt.savefig(f'./plots/n{n}_solid_collision.png')
        plt.clf()
        mean["particle_collision"][::100].plot(yerr=std["particle_collision"][::100])
        plt.xlabel("Número de iteracion")
        plt.ylabel('Cantidad de colisiones entre particulas')
        plt.savefig(f'./plots/n{n}_particle_collision.png')
        plt.clf()

        total_collision = mean["particle_collision"] + mean["solid_collision"]

        total_collision[::100].plot(yerr=(std['solid_collision'] + std['particle_collision'])[::100])
        plt.xlabel("Número de iteracion")
        plt.ylabel('Cantidad de colisiones totales')
        # plt.show()
        plt.savefig(f'./plots/n{n}_total_collision.png')
        plt.clf()


