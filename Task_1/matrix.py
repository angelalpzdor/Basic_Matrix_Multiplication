import random

def multiplicar_matrices(n):
    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]


    B_T = [[B[j][i] for j in range(n)] for i in range(n)]

    C = [[0.0 for _ in range(n)] for _ in range(n)]
    for i in range(n):
        for j in range(n):
            add = 0.0
            for k in range(n):
                add += A[i][k] * B_T[j][k]
            C[i][j] = add
    return C
