package com.example.calculimc
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        val buttonCalculer: Button =
            findViewById(R.id.buttonCalculer)
        val buttonEffacer: Button =
            findViewById(R.id.buttonEffacer)

        buttonCalculer.setOnClickListener {
            calculerIMC()
        }

        buttonEffacer.setOnClickListener {
            effacer()
        }
    }

    private fun calculerIMC() {
        val poidsTexte = editTextPoids.text.toString().trim()
        val tailleTexte = editTextTaille.text.toString().trim()

        // Vérifier que les champs ne sont pas vides
        if (poidsTexte.isEmpty() || tailleTexte.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.erreur_champs),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Accepter le point ou la virgule décimale
        val poids = poidsTexte.replace(',', '.').toDoubleOrNull()
        val taille = tailleTexte.replace(',', '.').toDoubleOrNull()

        // Vérifier que les saisies sont numériques
        if (poids == null || taille == null) {
            Toast.makeText(
                this,
                getString(R.string.erreur_nombre),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Vérifier les valeurs strictement positives
        if (poids <= 0.0 || taille <= 0.0 ||
            poids.isInfinite() || poids.isNaN() ||
            taille.isInfinite() || taille.isNaN()
        ) {
            Toast.makeText(
                this,
                getString(R.string.erreur_positif),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Calcul de l'IMC
        val imc = poids / (taille * taille)

        // Affichage avec deux chiffres après la virgule
        val imcFormate = String.format(Locale.FRANCE, "%.2f", imc)
        textViewImc.text = getString(
            R.string.resultat_imc,
            imcFormate
        )

        // Interprétation et couleur
        val categorie: String
        val couleur: Int

        if (imc < 18.5) {
            categorie = getString(R.string.cat_insuffisance)
            couleur = Color.rgb(255, 152, 0) // Orange
        } else if (imc < 25) {
            categorie = getString(R.string.cat_normale)
            couleur = Color.rgb(46, 125, 50) // Vert
        } else if (imc < 30) {
            categorie = getString(R.string.cat_surpoids)
            couleur = Color.rgb(255, 152, 0) // Orange
        } else if (imc < 35) {
            categorie = getString(R.string.cat_obesite_moderee)
            couleur = Color.rgb(211, 47, 47) // Rouge
        } else if (imc < 40) {
            categorie = getString(R.string.cat_obesite_severe)
            couleur = Color.rgb(211, 47, 47) // Rouge
        } else {
            categorie = getString(R.string.cat_obesite_morbide)
            couleur = Color.rgb(136, 14, 79) // Rouge foncé
        }

        textViewCategorie.text = categorie
        textViewCategorie.setTextColor(couleur)
    }

    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()

        textViewImc.text = getString(R.string.resultat_initial)
        textViewCategorie.text = getString(R.string.categorie_initiale)
        textViewCategorie.setTextColor(Color.BLACK)

        editTextPoids.requestFocus()
    }
}
