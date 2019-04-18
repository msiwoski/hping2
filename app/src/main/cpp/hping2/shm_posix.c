#include <stddef.h>
#include <stdio.h>
#include <sys/shm.h>
#include "shm.h"

static int id;	/* shared memory id */

int shm_creat(int size)
{
	id = shmget(IPC_PRIVATE, size, IPC_CREAT | 0777);
	if (id == -1)
	{
		perror("[shm_creat] shmget");
		return -1; /* on error -1 */
	}
	return id; /* on success > 0 */
}

void *shm_attach(void)
{
	void *shared;

	shared = shmat(id, 0, 0);
	if (shared == (void*) -1)
	{
		perror("[shm_attach] shmat");
		return NULL; /* on error NULL */
	}
	return shared; /* on success the address */
}

int shm_rm(void)
{
	struct shmid_ds shmemds;

	return shmctl(id, IPC_RMID, &shmemds);
}

int shm_detach(void *addr)
{
	return shmdt(addr);
}

void *shm_init(int size)
{
	if (shm_creat(size) == -1)
		return NULL;
	return shm_attach();
}

void shm_close(void *addr)
{
	shm_detach(addr);
	shm_rm();
}

