package graphdemo;

/**
 * A testbed for a weighted digraph abstract data type implementation
 * and implementations of some classical graph algorithms that use the
 * ADT
 * @see GraphAPI, Graph, City
 * @author Duncan, YOUR NAME
 * <pre>
 * usage: GraphDemo <graphFileName>
 * <graphFileName> - a text file containing the description of the graph
 * in DIMACS file format
 * Date: 99-99-99
 * course: csc 3102
 * programming project 3
 * Instructor: Dr. Duncan
  *
 * DO NOT REMOVE THIS NOTICE (GNU GPL V2):
 * Contact Information: duncanw@lsu.edu
 * Copyright (c) 2021 William E. Duncan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 * </pre>
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.Comparator;
import java.util.PriorityQueue; 
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphDemo
{
   public static final Double INFINITY = Double.POSITIVE_INFINITY;

   public static void main(String []args) throws GraphException
   {
      if (args.length != 1)
      {
         System.out.println("Usage: GraphDemo <filename>");
         System.exit(1);
      }
      City c1, c2;
      Scanner console;
      int menuReturnValue, i,j;
      Function<City,PrintStream> f = aCity -> System.out.printf("%-2d  %-30s%n",aCity.getKey(),aCity.getLabel().trim());      
      Graph<City> g = readGraph(args[0]);      
      Graph<City> gComp = complement(g);
      long s = g.size();
      menuReturnValue = -1;
      while (menuReturnValue != 0)
      {
         menuReturnValue = menu();
         switch(menuReturnValue)
         {
             case 1: //Traversal BFS(G) and DFS(G')
                //invoke the relevant traversal method
                // Output should be aligned in two-column format as illustrated below:
                // 1     Charlottetown
                // 4     Halifax
                // 2     Edmonton                   
                System.out.println();
                System.out.println("BFS Traversal of the Graph In "+args[0]);
                System.out.println("==========================================================================");   
                //invoke the bfsTraverse method

                System.out.println("==========================================================================");
                System.out.println();               
                System.out.println("PostOrder DFS Traversal of the Graph In "+args[0]);
                System.out.println("==========================================================================");
                //invoke the dfsTraverse method  

                System.out.println("==========================================================================");
                System.out.println();
                System.out.println();
                break;          
             case 2: //Check if a digraph is strongly connected
                System.out.println();
                //Add code here to output:
                //"The digraph in <filename> is strongly connected.",
                //if the digraph is strongly connected, and 
                //"The digraph in <filename> is not strongly connected.",
                //if the digraph is not strongly connected.


                //End add code here
                System.out.printf("%n%n");
                break;                 
            case 3://Shortest-path algorithm
                console = new Scanner(System.in);
                System.out.printf("Enter the source vertex: ");      
                int initial = console.nextInt();
                System.out.printf("Enter the destination vertex: ");      
                int dest = console.nextInt();
                if (g.isPath(new City(initial), new City(dest)) && g.isPath(new City(dest), new City(initial)))
                {
                   System.out.printf("Shortest round trip from %s to %s:%n",g.retrieveVertex(new City(initial)).getLabel().trim(),g.retrieveVertex(new City(dest)).getLabel().trim());				   
                   System.out.println("=========================================================================================");
                   //Add code here to print each leg of the trip from the source to the destination
                   //using the format below, where the columns are left-aligned and the distances
                   //are displayed to the nearest hundredths.
                   //For example:
                   //Baton Rouge -> New Orleans:
                   //Baton Rouge            ->   Gonzales                  10.20 mi
                   //Gonzales               ->   Metaire                   32.00 mi
                   //Metaire                ->   New Orleans                7.25 mi
                   //Distance: 49.75 mi
                   //
                   //New Orleans -> Baton Rouge
                   //New Orleans            ->   Metaire                    8.00 mi
                   //Metaire                ->   Gonzales                  33.00 mi
                   //Gonzalen               ->   Baton Rouge               10.00 mi
                   //Distance: 51.00 mi
                   //==============================================================
                   //Round Trip Distance: 100.75 mi
                   
				   
				   //End code                     
                }
                else
                   System.out.printf("There is no path.%n%n");
                break;              
            case 4: //in-deg topoSort of V(G)
               System.out.println();
               int[] top = topSortInDeg(g);
               if (top != null)
               {
                   System.out.println("Topological Sorting of The Graph In "+args[0]);
                   System.out.println("==========================================================================");           
                   for (i=1; i<=g.size(); i++)
                   {
                       c1 = g.retrieveVertex(new City(top[i-1]));
                       f.apply(c1);
                   }
                   System.out.println("==========================================================================");
               }
               else
                   System.out.println("No topological ordering possible. The digraph in "+args[0]+" contains a directed cycle.");
               System.out.printf("%n%n");
               break;
            default: ;
         } //end switch
      }//end while
   }//end main

   /**
    * This method reads a text file formatted as described in the project description.
    * @param filename the name of the DIMACS formatted graph file.
    * @return an instance of a graph.
    */
   private static Graph<City> readGraph(String filename)
   {
      try
      {
         Graph<City> newGraph = new Graph();
         try (FileReader reader = new FileReader(filename)) 
         {
            char temp;
            City c1, c2, aCity;
            String tmp;
            int k, m, v1, v2, j, size=0, nEdges=0;
            Integer key, v1Key,v2Key;
            Double weight;
            Scanner in = new Scanner(reader);
            while (in.hasNext())
            {
                tmp = in.next();
                temp = tmp.charAt(0);
                if (temp == 'p')
                {
                    size = in.nextInt();
                    nEdges = in.nextInt();
                }
                else if (temp == 'c')
                {
                    in.nextLine();
                }
                else if (temp == 'n')
                {
                    key = in.nextInt();
                    tmp = in.nextLine();
                    aCity = new City(key,tmp);
                    newGraph.insertVertex(aCity); 
                }
                else if (temp == 'e')
                {
                    v1Key = in.nextInt();
                    v2Key = in.nextInt();
                    weight = in.nextDouble();
                    c1 = new City(v1Key);
                    c2 = new City(v2Key);
                    newGraph.insertEdge(c1,c2,weight); 
                }
            }
         }
         return newGraph;
      }
      catch(IOException exception)
      {
            System.out.println("Error processing file: "+exception);
      }
      return null;
   } 

   /**
    * Display the menu interface for the application.
    * @return the menu option selected.
    */  
   private static int menu()
   {
      Scanner console = new Scanner(System.in);
      //int option;
      String option;
      do
      {
         System.out.println("         BASIC WEIGHTED GRAPH APPLICATION   ");
         System.out.println("==================================================");
         System.out.println("[1] Traversal BFS(G) and DFS(G)");
         System.out.println("[2] Check G for Strong Connectivity");
         System.out.println("[3] Dijkstra's Shortest Round Trip in G");
         System.out.println("[4] Topological Ordering of V(G)");
         System.out.println("[0] Quit");
         System.out.println("==================================================");
         System.out.printf("Select an option: ");         
         option = console.nextLine().trim();
         try
         {
             int choice = Integer.parseInt(option);
             if (choice < 0 || choice > 4)
             {
                System.out.println("Invalid option...Try again");
                System.out.println();
             }
             else
                return choice;
         }
         catch(NumberFormatException e)
         {
            System.out.println("Invalid option...Try again");
         }                           
      }while(true);
   }
   

   /**
    * This method computes the cost and path arrays using the 
    * Dijkstra's single-source shortest path greedy algorithm.
    * @param g an instance of a weighted directed graph
    * @param dist an array containing shortest distances from a source vertex
    * @param pred an array containing predecessor vertices along the shortest path
    * @throws GraphException on call to retrieveEdge on non-existent edge
    */
   private static void dijkstra(Graph<City> g, double[] dist, int[] pred, int source, int destination) throws GraphException
   {
       //Implement this method
       int n = (int)g.size();
       boolean[] seen = new boolean[n];
       class Node
       {
           public int id;
           public double key;
           public Node() {}
           public Node(int v, double k)
           {
               id = v;
               key = k;
           }           
       }
       Comparator<Node> cmp = (v1, v2) -> 
       {
           double d = v1.key - v2.key;
           if (d < 0)
               return -1;
           if (d > 0)
               return 1;
           return v1.id - v2.id;         
       };
      //Defining an instance of the PriorityQueue class that uses the comparator
       //and complete the implementation of the algorithm
       for(Vertex v = g.first; v == g && v != source; v = pNextVertex){
           
       }
       source.key = 0;
       int[] q = pred;
       
       while(q.isEmpty = false && q.top != dist){
           q.top = q.min;
           dist(top) = top.key;
           for(v = adj[top]){
               if(q.v && top.key + weight(top, v) < v.key){
                   DecreaseKey(Q,v,v.key + weight(top,v));
                   pred[v] = top;
                   dist[v] = v.key                         

               }
           }
           
       }
       
       
       
   }

  /**
    * Generates a topological labeling of the specified graph by repeatedly
    * selecting a vertex with 0 in-degree and then removing the vertex and all
    * its out-going edges from the graph. It explores the digraph in lexicographical
    * order when adding a new vertex to the topological ordering.
    * @param g a digraph
    * @return an array containing topological ordering of the vertices of the specified digraph if one
    * exists; otherwise, null
    * @throws GraphException when the specified digraph is empty
    * <pre>
    * Note: This method should not mutate (modify) the original graph in anyway;
    * </pre>
    */
   private static int[] topSortInDeg(Graph<City> g) throws GraphException
   {
      //implement this method
      //In-degree-based topological sort

       return null;
   }   
   
   /**
    * Determines whether the digraph is strongly connected
    * @param g a digraph
    * @return true is there is a directed path between every
    * pair of vertices; otherwise, false
    */
   public static boolean isStronglyConnected(Graph g)
   {
       if(g.Edge == null){
           return true;
       }
        else{
            Vertex tmp = g.first;
            int i = 1;
            int j = 1;
            int gFactorial = 0;
            while(j < 1)
                j = 1;
                while(i < (g.size - j - 1){
                    tmp = pNextEdge;
                    gFactorial = gFactorial + 1;
                    i = i + 1;
                }
                j = j + 1;
            }
            
            Edge tmpEdge = g.first;
            int edgeCount = 0
            while(tmpEdge != null){
                tmpEdge = pNextEdge;
                edgeCount = edgeCount + 1;
            }
                    
            if(gFactorial == edgeCount){
                return true;
            }
            else{
            return false;
            }
        }
   }
}

