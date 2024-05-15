#include<stdio.h>
#include<stdlib.h>
#include<malloc.h>
#include <stdbool.h>

typedef struct haffman_node{
    int weight;
    struct haffman_node* next;
    struct haffman_node*lchild;
    struct haffman_node*rchild;
}Hnode;

void creat_haffman_linklist(Hnode ** Head,int n){
    Hnode *temp=(Hnode *) malloc(sizeof (Hnode));
    *Head=temp;
    (*Head)->next=NULL;
    Hnode *r=*Head;
    int x;
    for(int i=0;i<n;i++){
        Hnode *temp=(Hnode *) malloc(sizeof (Hnode));
        temp->lchild=temp->rchild=NULL;
        r->next=temp;
        scanf("%d",&x);
        temp->weight=x;
        r=temp;
    }
    r->next=NULL;
}

void sort_haffman_linklist(Hnode *head)
{
    Hnode *p=(*head).next;
    while(p)
    {
        int min=p->weight;Hnode *position=NULL;
        Hnode *p1=p->next;
        while(p1)
        {
            if(p1->weight<min) {min=p1->weight;position=p1;}
            p1=p1->next;
        }
        if(position!=NULL)position->weight=p->weight;
        p->weight=min;
        p=p->next;
    }
}

void display_haffman_linklist(Hnode *head)
{
    Hnode *p=head->next;
    while(p){printf("%d",p->weight);p=p->next;}
}

void ChangeWeightList(Hnode *weight, Hnode *new_node) {
    weight->next = weight->next->next->next;
    Hnode *pre = weight;
    Hnode *p = weight->next;

    while (p) {
        if (new_node->weight <= weight->next->weight) {
            pre->next = new_node;
            new_node->next = p;
            return;
        }
        if (p->weight < new_node->weight) {
            pre = pre->next;
            p = p->next;
        } else {
            pre->next = new_node;
            new_node->next = p;
            return;
        }
    }

    pre->next = new_node;
}

void create_huffman_tree(Hnode *weight, Hnode **root) {
    if (weight->next == NULL) return;

    Hnode *p = weight->next;
    Hnode *first, *second;

    if (p->lchild == NULL && p->rchild == NULL) {
        first = (Hnode*)malloc(sizeof(Hnode));
        first->weight = p->weight;
        first->next = first->lchild = first->rchild = NULL;
    } else {
        first = p;
    }

    p = p->next;

    if (p->lchild == NULL && p->rchild == NULL) {
        second = (Hnode*)malloc(sizeof(Hnode));
        second->weight = p->weight;
        second->next = second->lchild = second->rchild = NULL;
    } else {
        second = p;
    }

    Hnode *new_node = (Hnode*)malloc(sizeof(Hnode));
    new_node->weight = first->weight + second->weight;
    new_node->lchild = first;
    new_node->rchild = second;
    new_node->next = NULL;

    if (p->next == NULL) {
        *root = new_node;
        return;
    }

    ChangeWeightList(weight, new_node);
    create_huffman_tree(weight, root);
}

void traversing_first_haffman(Hnode *root) {
    if (root != NULL) {
        printf("%d ", root->weight);
        traversing_first_haffman(root->lchild);
        traversing_first_haffman(root->rchild);
    }
}

int main()
{
    Hnode *head;Hnode *root;
    int n; scanf("%d" ,&n);
    creat_haffman_linklist(&head,n);
    sort_haffman_linklist(head);//对节点排序
    create_huffman_tree(head,&root);
    traversing_first_haffman(root);
}

