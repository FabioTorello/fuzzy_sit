package it.emarolab.fuzzySIT.semantic;

import fuzzydl.AllInstancesQuery;
import fuzzydl.ConfigReader;
import fuzzydl.KnowledgeBase;
import fuzzydl.exception.FuzzyOntologyException;
import fuzzydl.milp.Solution;
import fuzzydl.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.util.*;
import fuzzydl.*;
import fuzzydl.exception.*;
import fuzzydl.milp.*;
import fuzzydl.parser.*;
import fuzzydl.util.*;
import it.emarolab.fuzzySIT.FuzzySITBase;

public class FuzzydlTest
{
    public static void main(String[] args) throws FuzzyOntologyException,
            InconsistentOntologyException,
            IOException, ParseException {

        ConfigReader.loadParameters( "src/main/resources/FuzzyDL_CONFIG", new String[0]);
        Parser parser = new Parser(new FileInputStream("src/test/resources/fuzzyDlTest.txt"));
        parser.Start();

        KnowledgeBase kb = parser.getKB();
        kb.solveKB();

        /*
        //kb.addConcept("S", new RightConcreteConcept("ATLEAST", 0, 100, 1, 2));
        MinSubsumesQuery q = new MinSubsumesQuery(kb.getConcept("Minor"), kb.getConcept("YoungPerson"),SubsumptionQuery.LUKASIEWICZ);
        Solution sol = q.solve( kb);
        if (sol.isConsistentKB())
            System.out.println(q.toString() + sol.getSolution());
        */
/*
        FuzzydlToOwl2 f = new FuzzydlToOwl2( "/home/bubx/FuzzyOWL2Tools/FuzzyDL/kb.txt", "/home/bubx/FuzzyOWL2Tools/FuzzyDL/kb.owl");
        f.run();
*/

        ArrayList <Query> queries = parser.getQueries();
        for( Query q : queries) {
            Solution result = q.solve(kb);
            if (result.isConsistentKB())
                System.out.println(q.toString() + result.getSolution());
// System.out.println(q.toString());
            else
               System.out.println("KB is inconsistent");
        }

    }
}
